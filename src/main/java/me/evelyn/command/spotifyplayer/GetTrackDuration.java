package me.evelyn.command.spotifyplayer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;

import java.io.IOException;

public class GetTrackDuration {

    public static int getTrackDurationSeconds(String trackID){
        String accessToken = ClientCredential.getClientToken();

        GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = new SpotifyApi
                .Builder()
                .setAccessToken(accessToken)
                .build()
                .getAudioFeaturesForTrack(trackID)
                .build();

        try{
            AudioFeatures audioFeatures = getAudioFeaturesForTrackRequest.execute();
            return (audioFeatures.getDurationMs() + 500) / 1000;
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e){
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        ClientCredential.clientCredentials_Sync();
        System.out.println(getTrackDurationSeconds("5Uh1uXAwxHdzeENqcBVZIH"));
    }
}
