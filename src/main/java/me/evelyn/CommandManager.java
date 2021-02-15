package me.evelyn;

import me.evelyn.command.CommandContext;
import me.evelyn.command.ICommand;
import me.evelyn.command.commands.botcommands.*;
import me.evelyn.command.commands.fun.GoogleCommand;
import me.evelyn.command.commands.fun.JokeCommand;
import me.evelyn.command.commands.fun.MemeCommand;
import me.evelyn.command.commands.music.*;
import me.evelyn.command.commands.servermanagement.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new PingCommand());
        addCommand(new BlacklistCommand());
        addCommand(new WhitelistCommand());
        addCommand(new SnipeCommand());
//        addCommand(new SnipeImageCommand());
        addCommand(new HelpCommand(this));
        addCommand(new TestCommand());

        addCommand(new MemeCommand());
        addCommand(new JokeCommand());

        addCommand(new DeleteCommand());
        addCommand(new KickCommand());
        addCommand(new GoogleCommand());

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
        addCommand(new ServerStatsCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        if(Settings.BLACKLIST.contains(Objects.requireNonNull(event.getMember()).getIdLong())){
            return;
        }


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
