package me.evelyn.command.spotifyplayer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.albums.GetAlbumsTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GetAlbumTracks {
    public static List<String> tracksInfo = new ArrayList<>();
    public static int trackNum = 0;

    public static void getAlbumsTracks_Sync(String albumId) {
        String accessToken = ClientCredential.getClientToken();

        GetAlbumsTracksRequest getAlbumsTracksRequest = new SpotifyApi
                .Builder()
                .setAccessToken(accessToken)
                .build()
                .getAlbumsTracks(albumId)
                .build();;

        try {
            final Paging<TrackSimplified> trackSimplifiedPaging;
            trackSimplifiedPaging = getAlbumsTracksRequest.execute();
            TrackSimplified[] tracks = trackSimplifiedPaging.getItems();
            for(TrackSimplified track : tracks){
                String[] trackInfo = track.toString().split(",");
                String songName = trackInfo[0].replace("TrackSimplified(name=", "");
                String artists = trackInfo[1].replace("artists=[ArtistSimplified(name=", "");
                String trackId = "";

                for (String s : trackInfo) {
                    if (s.contains("tracks/")) {
                        trackId = s.replace("href=https://api.spotify.com/v1/tracks/", "").replace(" ", "");
                    }
                }

                tracksInfo.add(songName + "," + artists + "," + trackId);
            }

            trackNum = trackSimplifiedPaging.getTotal();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientCredential.clientCredentials_Sync();
        getAlbumsTracks_Sync("76J6QD8LjYGoAd5mgGlLAN");
    }
}
