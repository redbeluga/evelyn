package me.evelyn.command.commands.events;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.evelyn.command.lavaplayer.GuildMusicManager;
import me.evelyn.command.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DisconnectFromVC extends ListenerAdapter {
    public void Disconnect(@Nonnull ReadyEvent event) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable checkForMusic = () -> {
            event.getJDA().getGuilds().forEach(guild -> {
                AudioManager audioManager = guild.getAudioManager();
                if (audioManager.getConnectedChannel() != null) {
                    if(PlayerManager.getInstance().getMusicManager(guild).audioPlayer.getPlayingTrack() == null){
                        try {
                            Thread.sleep(30000);
                        }
                        catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        if(PlayerManager.getInstance().getMusicManager(guild).audioPlayer.getPlayingTrack() == null){
                            audioManager.closeAudioConnection();
                            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
                            musicManager.scheduler.player.stopTrack();
                            musicManager.scheduler.queue.clear();
                        }
                    }
                }
            });
        };

        Runnable timer = () -> {
            event.getJDA().getGuilds().forEach(guild -> {
                AudioManager audioManager = guild.getAudioManager();
                if (audioManager.getConnectedChannel() != null) {
                    if (audioManager.getConnectedChannel().getMembers().size() == 1) {
                        audioManager.closeAudioConnection();
                        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
                        musicManager.scheduler.player.stopTrack();
                        musicManager.scheduler.queue.clear();
                    }
                }
            });
        };


        Runnable checkwhetherinchannelalone = () -> {
            event.getJDA().getGuilds().forEach(guild -> {
                final AudioPlayer audioPlayer = PlayerManager.getInstance().getMusicManager(guild).audioPlayer;
                AudioManager audioManager = guild.getAudioManager();
                if (audioManager.getConnectedChannel() != null) {
                    if (audioManager.getConnectedChannel().getMembers().size() == 1 || audioPlayer.getPlayingTrack() == null) {
                        executor.schedule(checkForMusic, 90, TimeUnit.SECONDS);
                        executor.schedule(timer, 60, TimeUnit.SECONDS);
                    }
                }
            });
        };

        executor.scheduleWithFixedDelay(checkwhetherinchannelalone, 0, 5, TimeUnit.SECONDS);
    }
}
