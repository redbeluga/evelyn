package me.evelyn.command.spotifyplayer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;

public class GetTrack {

    public static String trackInfo = "";

    public static void getTrackInfo(String trackId){
        String accessToken = ClientCredential.getClientToken();

        GetTrackRequest getTrackRequest = new SpotifyApi
                .Builder()
                .setAccessToken(accessToken)
                .build()
                .getTrack(trackId)
                .build();

        try{
            Track track = getTrackRequest.execute();
            String[] trackNameandArtist = track.toString().split(",");
            String songName = "" + trackNameandArtist[0].replace("Track(name=", "");
            String artists = "" + trackNameandArtist[1].replace("artists=[ArtistSimplified(name=", "");

            trackInfo = (songName + "," + artists + "," + trackId);
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientCredential.clientCredentials_Sync();
        getTrackInfo("2ckGH6FtxWeAv0SskcREd1");
        System.out.println(trackInfo);
    }

}
