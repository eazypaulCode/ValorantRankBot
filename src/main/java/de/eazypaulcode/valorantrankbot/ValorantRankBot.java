package de.eazypaulcode.valorantrankbot;

import de.eazypaulcode.valorantrankbot.command.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class ValorantRankBot {

    private Logger logger;

    private String botToken;
    private CommandManager commandManager;

    private JDA bot;

    public ValorantRankBot(String botToken) {
        this.logger = LoggerFactory.getLogger("ValorantRankBot");
        this.botToken = botToken;
        init();
    }

    private void init() {
        logger.info("Bot wird gestartet...");
        this.commandManager = new CommandManager();
        try {
            JDABuilder builder = JDABuilder.createDefault(botToken);
            builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            builder.setBulkDeleteSplittingEnabled(false);
            bot = builder.build().awaitReady();
        } catch (LoginException e) {
            logger.error("Der angegebene Bot Token ist nicht gültig!");
        } catch (InterruptedException e) {
            logger.error("Der Bot wurden beim Starten unterbrochen!");
        }
        commandManager.load(this);
    }

    public JDA getBot() {
        return bot;
    }

    public Logger getLogger() {
        return logger;
    }
}
