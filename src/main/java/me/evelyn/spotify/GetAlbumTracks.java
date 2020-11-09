package me.evelyn.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.albums.GetAlbumsTracksRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;


public class GetAlbumTracks {
    private static String accessToken = "";
    private static final String id = "50BlleBqX2CiTpTjvpCgPh";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();
    private static final GetAlbumsTracksRequest getAlbumsTracksRequest = spotifyApi.getAlbumsTracks(id)
//          .limit(10)
//          .offset(0)
//          .market(CountryCode.SE)
            .build();

    public static void getAlbumsTracks_Sync() {
        try {
            final Paging<TrackSimplified> trackSimplifiedPaging = getAlbumsTracksRequest.execute();

            System.out.println("Total: " + trackSimplifiedPaging.getTotal());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientCredential.clientCredentials_Sync();
        accessToken = ClientCredential.getClientToken();
        System.out.println(accessToken);
        getAlbumsTracks_Sync();
    }
}
