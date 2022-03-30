package de.eazypaulcode.valorantrankbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageListeners implements EventListener {

    private final long MONITOR_GUILD_ID = 955473272068268063L;

    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof MessageReceivedEvent event) {
            if (event.getGuild().getIdLong() != MONITOR_GUILD_ID) return;
            if (event.getChannelType() != ChannelType.TEXT) return;
            TextChannel channel = event.getTextChannel();
            if (event.getMember().getUser().isBot()) return;
            if (channel.getParentCategory() == null) return;
            if (channel.getParentCategory().getName().contains("Monitor")) {
                event.getMessage().delete().queue();
            }
            if (channel.getParentCategory().getName().contains("Commands")) {
                event.getMessage().replyEmbeds(new EmbedBuilder()
                                .setTitle(":no_entry: | **Message Command**")
                                .appendDescription("**Dieser Channel ist nicht für Message Commands gedacht.**\n")
                                .appendDescription("*<@955480768577159249> funktioniert nur mit Interaction Commands*")
                                .setFooter("Löschung in 3 Sekunden")
                                .setColor(Color.RED)
                                .build())
                        .queue(message -> {
                            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                            executorService.schedule(() -> {
                                message.delete().queue();
                                event.getMessage().delete().queue();
                                executorService.shutdown();
                            }, 3, TimeUnit.SECONDS);
                        });
            }
        }
    }
}
