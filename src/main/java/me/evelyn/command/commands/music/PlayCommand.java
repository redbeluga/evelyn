package me.evelyn.command.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.evelyn.Config;
import me.evelyn.Listener;
import me.evelyn.command.CommandContext;
import me.evelyn.command.HelperMethods;
import me.evelyn.command.ICommand;
import me.evelyn.command.commands.spotifymusic.SpotifyPlayCommand;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final YouTube youTube;
    private static final String[] ytKeys = {
            "youtube_key1",
            "youtube_key2",
            "youtube_key3"
    };

    public PlayCommand(){
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("evelyn")
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }

        youTube = temp;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("What am I going to play, doofus?").queue();
            return;
        }
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!selfVoiceState.inVoiceChannel())
            JoinCommand.join(ctx);
        else {
            if (!memberVoiceState.inVoiceChannel()) {
                channel.sendMessage("What voice channel am I going to join if you aren't even in one?").queue();
                return;
            }

            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                channel.sendMessage("I'm in another voice channel. Think about your friends, smart one.").queue();
                return;
            }
        }
        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)) {
            String ytSearched = searchYoutube(link, ctx);
            if(ytSearched == null){
                channel.sendMessage("I can't find that on YouTube.").queue();
                return;
            }

            link = ytSearched;
        }

        else{
            try {
                if(HelperMethods.getHost(link).equalsIgnoreCase("open.spotify.com")){
                    SpotifyPlayCommand sp = new SpotifyPlayCommand();
                    sp.handle(ctx);
                    return;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }



        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();
        
        PlayerManager.getInstance()
                .loadAndPlay(link, track != null, ctx, false);
    }


    @Override
    public String getName() {
        return "play";
    }

    @Override
    public List<String> getAliases() {
        return List.of("p", "play");
    }

    private boolean isUrl(String url) {
        String urlRegex = "((http:\\/\\/|https:\\/\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    @Nullable
    private String searchYoutube(String input, CommandContext ctx) {
        for (String key : ytKeys) {
            try {
                List<SearchResult> results = youTube.search()
                        .list("id,snippet")
                        .setQ(input)
                        .setMaxResults(4L)
                        .setType("video")
                        .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                        .setKey(Config.get(key))
                        .execute()
                        .getItems();

                if (!results.isEmpty()) {

                    String videoId = results.get(0).getId().getVideoId();

                    return "https://www.youtube.com/watch?v=" + videoId;
                }
            } catch (Exception e) {
                LOGGER.error(ctx.getGuild().getName() + ": " + e.getMessage());
            }
        }
        return null;
    }
}
