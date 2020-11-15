package me.evelyn.command.commands.spotifymusic;

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

import javax.annotation.Nullable;
import java.net.URISyntaxException;
import java.util.List;

import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import me.evelyn.command.spotifyplayer.GetAlbumTracks;
import me.evelyn.command.spotifyplayer.GetPlayListTracks;
import me.evelyn.command.spotifyplayer.GetTrack;
import me.evelyn.command.spotifyplayer.GetTrackDuration;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpotifyPlayCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

    private final YouTube youTube;
    private static final String[] ytKeys = {
            "youtube_key1",
            "youtube_key2",
            "youtube_key3"
    };

    public SpotifyPlayCommand(){
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

    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(ctx.getArgs().size() != 1){
            channel.sendMessage("What am I going to look up? Provide a link, jeez.").queue();
            return;
        }

        String link = ctx.getArgs().get(0);
        String Id = "";
        String type = "";

        try {
            if(!HelperMethods.getHost(link).equalsIgnoreCase("open.spotify.com")){
                channel.sendMessage("It's Spotify play, so give me a Spotify link.").queue();
                return;
            }
            if(!HelperMethods.linktoPath(link).equalsIgnoreCase("playlist")){
                if(!HelperMethods.linktoPath(link).equalsIgnoreCase("track")) {
                    if(!HelperMethods.linktoPath(link).equalsIgnoreCase("album")) {
                        channel.sendMessage("Give some actual songs for me to play.");
                        return;
                    }
                }
            }
            type=HelperMethods.linktoPath(link);

            Id = HelperMethods.linktoId(link);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        if(type.equalsIgnoreCase("playlist"))
            loadPlaylist(ctx, Id);
        else {
            if(type.equalsIgnoreCase("album")){
                loadAlbum(ctx, Id);
            }
            else {
                loadTrack(ctx, Id);
            }
        }
    }

    @Nullable
    private String searchYoutube(String input, String trackId, CommandContext ctx) {
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
                    SearchResult bestResult = results.get(0);
                    int spotifyDuration = GetTrackDuration.getTrackDurationSeconds(trackId);

                    for(int i = 0; i < results.size(); i ++){
                        SearchResult result = results.get(i);
                        int ytDuration = HelperMethods.ytGetDuration(result.getId().getVideoId(), Config.get("key"));
                        if(Math.abs(ytDuration-spotifyDuration) < Math.abs(HelperMethods.ytGetDuration(bestResult.getId().getVideoId(), Config.get("key")) - spotifyDuration)){
                            bestResult = result;
                        }
                    }

                    String videoId = bestResult.getId().getVideoId();

                    return "https://www.youtube.com/watch?v=" + videoId;
                }
                else{
                    ctx.getChannel().sendMessage("Nothing came up on Youtube for: " + input);
                    return null;
                }
            } catch (Exception e) {
                LOGGER.error(ctx.getGuild().getName() + ": " + e.getMessage());
            }
        }
        ctx.getChannel().sendMessage("An error occurred. Try again later.");
        return null;
    }

    @Nullable
    private void loadPlaylist(CommandContext ctx, String playlistId){
        GetPlayListTracks.setPlaylistId(playlistId);
        GetPlayListTracks.getPlaylistTracks_Sync();
        List<String> tracksInfo = GetPlayListTracks.tracksInfo;
        new Thread(new Runnable() {
            public void run() {
                for (String trackInfo : tracksInfo){
                    String[] split = trackInfo.split(",");

                    String songName = split[0];
                    String artistNames = split[1];
                    String trackId = split[2];
                    String link = searchYoutube(songName + " " +  artistNames, trackId, ctx);

                    final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
                    final AudioPlayer audioPlayer = musicManager.audioPlayer;
                    final AudioTrack track = audioPlayer.getPlayingTrack();

                    PlayerManager.getInstance()
                            .loadAndPlay(link, track != null, ctx, true);
                }
            }
        }).start();

        ctx.getChannel().sendMessageFormat("**%s Songs Added to the Queue**", GetPlayListTracks.trackNum).queue();
    }

    @Nullable
    private void loadAlbum(CommandContext ctx, String albumId){
        GetAlbumTracks.getAlbumsTracks_Sync(albumId);
        List<String> tracksInfo = GetAlbumTracks.tracksInfo;
        new Thread(new Runnable() {
            public void run() {
                for (String trackInfo : tracksInfo) {
                    String[] split = trackInfo.split(",");

                    String songName = split[0];
                    String artistNames = split[1];
                    String trackId = split[2];
                    String link = searchYoutube(songName + " " + artistNames, trackId, ctx);

                    final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
                    final AudioPlayer audioPlayer = musicManager.audioPlayer;
                    final AudioTrack track = audioPlayer.getPlayingTrack();

                    PlayerManager.getInstance()
                            .loadAndPlay(link, track != null, ctx, true);
                }
            }
        }).start();

        ctx.getChannel().sendMessageFormat("**%s Songs Added to the Queue**", GetAlbumTracks.trackNum).queue();
    }

    @Nullable
    private void loadTrack(CommandContext ctx, String trackId){
        GetTrack.getTrackInfo(trackId);
        String trackInfo = GetTrack.trackInfo;

        String[] split = trackInfo.split(",");
        String songName = split[0];
        String artistNames = split[1];

        String link = searchYoutube(songName + " " +  artistNames, trackId, ctx);

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        PlayerManager.getInstance()
                .loadAndPlay(link, track != null, ctx, false);
    }
}
