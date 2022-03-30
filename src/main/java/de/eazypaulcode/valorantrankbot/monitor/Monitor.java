package de.eazypaulcode.valorantrankbot.monitor;

import de.eazypaulcode.valorantrankbot.ValorantRankBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Monitor {

    private final long ADDED_CHANNEL_ID = 958683794028781668L;
    private final long REMOVED_CHANNEL_ID = 958695791030059009L;

    private final long REQUEST_CHANNEL_ID = 958683035266609162L;

    private final ValorantRankBot rankBot;

    public Monitor(ValorantRankBot rankBot) {
        this.rankBot = rankBot;
    }

    public void sendAdded(Guild guild) {
        TextChannel textChannel = rankBot.getBot().getTextChannelById(ADDED_CHANNEL_ID);

        if (textChannel == null) {
            return;
        }

        List<Invite> invites = new ArrayList<>();
        Member owner = null;

        if (guild.getBotRole() != null) {
            if (guild.getBotRole().hasPermission(Permission.MANAGE_SERVER)) {
                invites.addAll(guild.retrieveInvites().complete());
            }
        }
        owner = guild.retrieveOwner().complete();

        textChannel.sendMessageEmbeds(new EmbedBuilder()
                .setTitle(MonitorType.GUILD_ADDED.getEmoji() + " | " + MonitorType.GUILD_ADDED.getDescription())
                .appendDescription("**Guild Name**: `" + guild.getName() + "`\n")
                .appendDescription("**Guild Invite**: `" + (invites.size() != 0 ? invites.get(0).getUrl()  : "`NO PERM / INVITE`") + "`\n")
                .appendDescription("**Guild ID**: `" + guild.getId() + "`\n")
                .appendDescription("**Guild Members**: `" + guild.getMemberCount() + "`\n")
                .appendDescription("**Guild Owner**: `" + (owner == null ? "`NO PERM`" : owner.getUser().getAsTag()) + "`")
                .setColor(Color.GREEN)
                .setFooter("Added on")
                .setTimestamp(Instant.now())
                .build()
        ).queue();
    }

    public void sendRemoved(Guild guild) {
        TextChannel textChannel = rankBot.getBot().getTextChannelById(REMOVED_CHANNEL_ID);

        if (textChannel == null) {
            return;
        }

        textChannel.sendMessageEmbeds(new EmbedBuilder()
                .setTitle(MonitorType.GUILD_REMOVED.getEmoji() + " | " + MonitorType.GUILD_REMOVED.getDescription())
                .appendDescription("**Guild Name**: `" + guild.getName() + "`\n")
                .appendDescription("**Guild ID**: `" + guild.getId() + "`\n")
                .setColor(Color.RED)
                .setFooter("Removed on")
                .setTimestamp(Instant.now())
                .build()
        ).queue();
    }

}
