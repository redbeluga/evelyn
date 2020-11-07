package me.evelyn.command.commands.music;

import com.wrapper.spotify.SpotifyApi;
import me.evelyn.Config;
import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;

import javax.annotation.Nullable;
import java.util.List;

public class SpotifyPlayCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        searchSpotify();
        ctx.getChannel().sendMessage("Something happened").queue();
    }

    @Override
    public String getName() {
        return "spotifyplay";
    }

    @Override
    public List<String> getAliases() {
        return List.of("sp", "spotifyplay");
    }

    @Nullable
    private String searchSpotify(){
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(Config.get("spotifyclientid"))
                .setClientSecret(Config.get("spotifyclientsecret"))
                .build();

        return null;
    }
}
