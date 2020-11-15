package me.evelyn.command.spotifyplayer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Album;
import com.wrapper.spotify.requests.data.albums.GetAlbumRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class GetAlbum {
    private static final String accessToken = "BQCxuc0SCIUsTtUaeaNZtrU7gUwslngJRAD1ZtFEYB55dKKV1uGQJ6UGdIB3ASQPwStUb8xuDYVppDxt_Fc";
    private static final String id = "5zT1JLIj9E57p3e1rFm9Uq";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();
    private static final GetAlbumRequest getAlbumRequest = spotifyApi.getAlbum(id)
//          .market(CountryCode.SE)
            .build();

    public static void getAlbum_Sync() {
        try {
            final Album album = getAlbumRequest.execute();

            System.out.println("Name: " + album.getName());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        getAlbum_Sync();
    }
}
