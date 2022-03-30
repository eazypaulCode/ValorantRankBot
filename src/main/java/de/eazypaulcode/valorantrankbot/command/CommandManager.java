package de.eazypaulcode.valorantrankbot.command;

import de.eazypaulcode.valorantrankbot.ValorantRankBot;
import de.eazypaulcode.valorantrankbot.command.commands.UserRankCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final List<CommandObject> list;

    public CommandManager() {
        this.list = new ArrayList<>();
    }

    public void load(ValorantRankBot rankBot) {
        list.add(new UserRankCommand(rankBot));


        CommandListUpdateAction commands = rankBot.getBot().updateCommands();

        for (CommandObject object : list) {
            commands = commands.addCommands(object.getCommand());
        }

        commands.queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (CommandObject object : list) {
            if (!object.getCommand().getName().equalsIgnoreCase(event.getInteraction().getName())) {
                continue;
            }

            object.respond(event);
        }
    }
}
