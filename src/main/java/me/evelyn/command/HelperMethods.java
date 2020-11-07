package me.evelyn.command;

import java.util.concurrent.TimeUnit;

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
}
