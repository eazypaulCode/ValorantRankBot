package de.eazypaulcode.valorantrankbot.command;

import de.eazypaulcode.valorantrankbot.ValorantRankBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class CommandObject {

    protected final ValorantRankBot rankBot;

    public CommandObject(ValorantRankBot rankBot) {
        this.rankBot = rankBot;
    }

    public abstract CommandData getCommand();

    public abstract void respond(SlashCommandInteractionEvent event);

}
