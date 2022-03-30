package de.eazypaulcode.valorantrankbot.listener;

import de.eazypaulcode.valorantrankbot.ValorantRankBot;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

public record MonitorListeners(ValorantRankBot rankBot) implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof GuildJoinEvent event) {
            rankBot.getBot().getPresence().setPresence(Activity.watching(rankBot.getBot().getGuilds().size() + " Servern zu."), false);
            rankBot.getMonitor().sendAdded(event.getGuild());
        }
        if (genericEvent instanceof GuildLeaveEvent event) {
            rankBot.getBot().getPresence().setPresence(Activity.watching(rankBot.getBot().getGuilds().size() + " Servern zu."), false);
            rankBot.getMonitor().sendRemoved(event.getGuild());
        }
    }
}
