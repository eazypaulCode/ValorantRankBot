package de.eazypaulcode.valorantrankbot.command.commands;

import de.eazypaulcode.valorantrankbot.ValorantRankBot;
import de.eazypaulcode.valorantrankbot.command.CommandObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.time.Instant;

public class UserRankCommand extends CommandObject {
    public UserRankCommand(ValorantRankBot rankBot) {
        super(rankBot);
    }

    @Override
    public CommandData getCommand() {
        return Commands.slash("userrank", "Fordere den Rank eines Accounts in Valorant an.")
                .addOption(OptionType.STRING, "username", "Nutzername (ohne Tag)", true)
                .addOption(OptionType.STRING, "tag", "Tag", true);
    }

    @Override
    public void respond(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null) {
            return;
        }

        Member member = event.getMember();

        if (member == null) {
            return;
        }

        OptionMapping username = event.getOption("username");
        OptionMapping tag = event.getOption("tag");

        if (username == null || tag == null) {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle(":warning: | Unbekannter Fehler")
                    .appendDescription("**Es ist ein unbekannter Fehler aufgetreten**\n")
                    .appendDescription("*Bitte versuche es naher erneut.*")
                    .setColor(Color.RED)
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        if (tag.getAsString().contains("#")) {
            event.replyEmbeds(new EmbedBuilder()
                    .setTitle(":warning: | Ungültiger Tag")
                    .appendDescription("**Bitte gib den Tag ohne # an**\n")
                    .setColor(Color.RED)
                    .build()
            ).setEphemeral(true).queue();
            return;
        }

        event.replyEmbeds(new EmbedBuilder()
                        .setTitle("<a:loading:393852367751086090> **Lade Antwort**")
                        .appendDescription("*Dies kann einen Moment dauern*")
                        .setColor(Color.CYAN)
                        .setFooter("Anfrage gesendet")
                        .setTimestamp(Instant.now())
                        .build())
                .setEphemeral(true)
                .queue(reactionHook -> {
                    HttpResponse<String> response;
                    response = Unirest.get("https://api.henrikdev.xyz/valorant/v1/mmr/eu/" + username.getAsString() + "/" + tag.getAsString() + "?=")
                            .asString();
                    switch (response.getStatus()) {
                        case 404: {
                            reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                    .setTitle(":warning: | Account")
                                    .appendDescription("**Dieser Account existiert nicht**\n")
                                    .appendDescription("*Bitte kontrolliere deine Angaben.*")
                                    .setColor(Color.RED)
                                    .build()
                            ).queue();
                            break;
                        }
                        case 429: {
                            reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                    .setTitle(":warning: | Rate-Limit")
                                    .appendDescription("**Es wurden zu viele Anfragen verschickt**\n")
                                    .appendDescription("*Bitte versuche es naher erneut.*")
                                    .setColor(Color.RED)
                                    .build()
                            ).queue();
                            break;
                        }
                        case 200: {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response.getBody());
                            } catch (JSONException e) {
                                reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                        .setTitle(":warning: | Fehlende Daten")
                                        .appendDescription("**Die API hat unvollständige Daten zurückgegeben**\n")
                                        .appendDescription("*Bitte versuche es erneut.*")
                                        .setColor(Color.RED)
                                        .build()
                                ).queue();
                                return;
                            }
                            if (!jsonObject.has("data")) {
                                reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                        .setTitle(":warning: | Fehlende Daten")
                                        .appendDescription("**Die API hat unvollständige Daten zurückgegeben**\n")
                                        .appendDescription("*Bitte versuche es erneut.*")
                                        .setColor(Color.RED)
                                        .build()
                                ).queue();
                                return;
                            }
                            JSONObject data;
                            try {
                                data = jsonObject.getJSONObject("data");
                            } catch (JSONException e) {
                                reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                        .setTitle(":warning: | Fehlende Daten")
                                        .appendDescription("**Die API hat unvollständige Daten zurückgegeben**\n")
                                        .appendDescription("*Bitte versuche es erneut.*")
                                        .setColor(Color.RED)
                                        .build()
                                ).queue();
                                return;
                            }
                            try {
                                reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                        .setTitle(":white_check_mark: | " + data.getString("name") + "#" + data.getString("tag"))
                                        .appendDescription("**Rank**: `" + data.getString("currenttierpatched") + "`\n")
                                        .setColor(Color.GREEN)
                                        .build()).queue();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                        default: {
                            reactionHook.editOriginalEmbeds(new EmbedBuilder()
                                    .setTitle(":warning: | Unbekannter Fehler")
                                    .appendDescription("**Es ist ein unbekannter Fehler aufgetreten**\n")
                                    .appendDescription("*Bitte versuche es naher erneut.*\n")
                                    .appendDescription("**Fehlercode**: `" + response.getStatus() + "`\n")
                                    .appendDescription("**Antwort der API**: `" + response.getBody() + "`")
                                    .setColor(Color.RED)
                                    .build()
                            ).queue();
                            break;
                        }
                    }
                });
    }
}
