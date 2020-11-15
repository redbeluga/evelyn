package me.evelyn.command.spotifyplayer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import me.evelyn.Config;
import me.evelyn.Listener;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientCredential {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private static final String clientId = Config.get("spotifyclientid");
    private static final String clientSecret = Config.get("spotifyclientsecret");
    private static String clientToken = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static void clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            LOGGER.info("Spotify client token is {}", clientCredentials.getAccessToken());
            clientToken = clientCredentials.getAccessToken();
            LOGGER.info("Expires in: {}", clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            LOGGER.info("Error: {}", e.getMessage());
        }
    }

    public static String getClientToken() {
        return clientToken;
    }
}
