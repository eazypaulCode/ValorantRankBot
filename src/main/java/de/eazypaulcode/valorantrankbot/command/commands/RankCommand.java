package de.eazypaulcode.valorantrankbot.command.commands;

import de.eazypaulcode.valorantrankbot.ValorantRankBot;
import de.eazypaulcode.valorantrankbot.command.CommandObject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class RankCommand extends CommandObject {
    public RankCommand(ValorantRankBot rankBot) {
        super(rankBot);
    }

    @Override
    public CommandData getCommand() {
        return Commands.slash("rank", "Fordere deinen Valorant Rank an.");
    }

    @Override
    public void respond(SlashCommandInteractionEvent event) {

    }
}
