package me.evelyn.command;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.client.http.HttpRequest;
import me.evelyn.Config;


public class HelperMethods {

    public static String formatTime(long timeInMillis){
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = (timeInMillis / TimeUnit.MINUTES.toMillis(1)) % 60;
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        if(hours == 0){
            if(minutes == 0){
                return String.format("0:%2d", seconds);
            }
            if(seconds == 0){
                return String.format("%2d:00", minutes);
            }
            return String.format("%d:%02d", minutes, seconds);
        }
        if(minutes == 0){
            return String.format("0:%02d", seconds);
        }

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static boolean isInteger(String s){
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static boolean isUrl(String url) {
        String urlRegex = "((http:\\/|https:\\/)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(\\/([a-zA-Z-_\\/\\.0-9#:?=&;,]*)?)?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public static String linktoId(String link) throws URISyntaxException {
        URI url = new URI(link);
        File path = new File(url.getPath());
        return path.getName();
    }

    public static String linktoPath(String link) throws URISyntaxException {
        URI url = new URI(link);
        String path = url.getPath();
        return path.split("/")[1];
    }

    public static String getHost(String link) throws URISyntaxException {
        URI url = new URI(link);
        return url.getHost();
    }

    public static int ytGetDuration(String videoId, String youtubekey) throws IOException {
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("video-test").build();

        YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
        videoRequest.setId(videoId);
        videoRequest.setKey(Config.get("youtube_key3"));
        VideoListResponse listResponse = videoRequest.execute();
        List<Video> videoList = listResponse.getItems();

        Video targetVideo = videoList.iterator().next();

        return ISOtoSeconds(targetVideo.getContentDetails().getDuration());
    }

    public static int ISOtoSeconds(String ISODuration){
        List<String> times = new ArrayList<>();
        int durationSec = 0;
        if (ISODuration.startsWith("PT")) {
            String ISODuration2 = ISODuration.replace("PT", "");
            if (ISODuration2.contains("H")) {
                times.add(ISODuration2.substring(0, ISODuration2.indexOf("H")) + "H");
            }
            if (ISODuration2.contains("M")) {
                times.add(ISODuration2.substring(ISODuration2.indexOf("H") + 1, ISODuration2.indexOf("M") + 1));
            }
            if (ISODuration2.contains("S")) {
                times.add(ISODuration2.substring(ISODuration2.indexOf("M") + 1, ISODuration2.indexOf("S") + 1));
            }
            for(String time : times){
                if(time.endsWith("S")){
                    durationSec += Integer.parseInt(time.substring(0, time.indexOf("S")));
                }
                if(time.endsWith("M")){
                    durationSec += Integer.parseInt(time.substring(0, time.indexOf("M"))) * 60;
                }
                if(time.endsWith("H")){
                    durationSec += Integer.parseInt(time.substring(0, time.indexOf("H"))) * 3600;
                }
            }
        }

        return durationSec;
    }

    public static void main(String[] args) throws IOException {
    }
}
