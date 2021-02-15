package me.evelyn.command.commands.music;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        join(ctx);
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getHelp() {
        return "Tells Evelyn to join the voice channel";
    }

    @Override
    public String getPackage() {
        return getClass().getPackageName().split("\\.")[getClass().getPackageName().split("\\.").length-1];
    }

    @Override
    public List<String> getAliases() {
        return List.of("join", "summon");
    }

    public static void join(CommandContext ctx){
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            if (memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                channel.sendMessage("I'm already in your voice channel. You want me to join it twice?").queue();
                return;
            }
            channel.sendMessage("I'm already in a voice channel. Think about your friends, smart one.").queue();
            return;
        }

        if(!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("What voice channel am I going to join if you aren't even in one?").queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if(!self.hasPermission(memberChannel, Permission.VOICE_CONNECT)) {
            channel.sendMessageFormat("I dont' have permissions to join `%s`", memberChannel.getName()).queue();
            return;
        }

        audioManager.openAudioConnection(memberChannel);
        channel.sendMessageFormat("**Joining**  `%s`", memberChannel.getName()).queue();
    }
}
