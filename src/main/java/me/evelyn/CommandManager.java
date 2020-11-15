package me.evelyn;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.commands.botcommands.PingCommand;
import me.evelyn.command.commands.fun.JokeCommand;
import me.evelyn.command.commands.fun.MemeCommand;
import me.evelyn.command.commands.music.*;
import me.evelyn.command.commands.servermanagement.DeleteCommand;
import me.evelyn.command.commands.servermanagement.KickCommand;
import me.evelyn.command.commands.servermanagement.SetPrefixCommand;
import me.evelyn.command.commands.servermanagement.ToggleFilterCommand;
import me.evelyn.command.commands.spotifymusic.SpotifyPlayCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());

        addCommand(new MemeCommand());
        addCommand(new JokeCommand());

        addCommand(new DeleteCommand());
        addCommand(new KickCommand());

        addCommand(new PlayCommand());
        addCommand(new JoinCommand());
        addCommand(new DisconnectCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new RemoveCommand());
        addCommand(new LoopCommand());
        addCommand(new SkipCommand());
        addCommand(new QueueCommand());
        addCommand(new ClearCommand());
        addCommand(new ShuffleCommand());

        addCommand(new SetPrefixCommand());
        addCommand(new ToggleFilterCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
