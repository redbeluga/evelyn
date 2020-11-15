package me.evelyn.command.spotifyplayer;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetPlayListTracks {
    public static List<String> tracksInfo = new ArrayList<>();
    public static int trackNum = 0;
    public static String playlistId = "";

    public static void getPlaylistTracks_Sync() {
        String accessToken = ClientCredential.getClientToken();
        GetPlaylistsItemsRequest getPlaylistsItemsRequest = new SpotifyApi
                .Builder()
                .setAccessToken(accessToken)
                .build()
                .getPlaylistsItems(playlistId)
                .build();
        try{
            final Paging<PlaylistTrack> trackPaging = getPlaylistsItemsRequest.execute();
            PlaylistTrack[] tracks = trackPaging.getItems();
            for(PlaylistTrack track : tracks){
                String[] trackInfo = track.getTrack().toString().split(",");
                String songName = trackInfo[0].replace("Track(name=", "");
                String artists = trackInfo[1].replace("artists=[ArtistSimplified(name=", "");
                String trackId = "";

                for(int i = 0; i < trackInfo.length; i ++){
                    if(trackInfo[i].contains("tracks/")){
                        trackId = trackInfo[i].replace("href=https://api.spotify.com/v1/tracks/", "").replace(" ","");
                    }
                }

                tracksInfo.add(songName + "," + artists + "," + trackId);
            }

            trackNum = trackPaging.getTotal();
        }catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e){
            System.out.println(e.getMessage());
        }
        playlistId = "";
    }

    public static void setPlaylistId(String playlistId) {
        GetPlayListTracks.playlistId = playlistId;
    }

    public static void main(String[] args) {
        ClientCredential.clientCredentials_Sync();
        setPlaylistId("0nwOpzYuSyQhl3OIAsOCuV");
        getPlaylistTracks_Sync();
    }
}
