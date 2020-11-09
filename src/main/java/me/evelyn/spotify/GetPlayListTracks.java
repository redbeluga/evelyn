package me.evelyn.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

import java.io.IOException;

public class GetPlayListTracks {
    private static final String accessToken = ClientCredential.getClientToken();
    private String playlistId = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();

    private final GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi
            .getPlaylistsItems(playlistId)
            .build();

    public GetPlayListTracks(String playlistId){
        this.playlistId = playlistId;
    }

    public String getPlaylistTracks_Sync() {
        try{
            final Paging<PlaylistTrack> trackPaging = getPlaylistsItemsRequest.execute();
            return trackPaging.getTotal().toString();
        }catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e){
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        GetPlayListTracks test = new GetPlayListTracks("0nwOpzYuSyQhl3OIAsOCuV");
        if(test.getPlaylistTracks_Sync() == null){
            System.out.println("Something broke");
            return;
        }
        System.out.println("Total: " + test.getPlaylistTracks_Sync());
    }
}
