package de.eazypaulcode.valorantrankbot;

import de.eazypaulcode.valorantrankbot.command.CommandManager;
import de.eazypaulcode.valorantrankbot.listener.MessageListeners;
import de.eazypaulcode.valorantrankbot.listener.MonitorListeners;
import de.eazypaulcode.valorantrankbot.monitor.Monitor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ValorantRankBot {

    private final Logger logger;

    private final String botToken;
    private CommandManager commandManager;

    private Monitor monitor;

    private JDA bot;

    public ValorantRankBot(String botToken) {
        this.logger = LoggerFactory.getLogger("ValorantRankBot");
        this.botToken = botToken;
        init();
    }

    private void init() {
        logger.info("Bot wird gestartet...");
        this.commandManager = new CommandManager();
        this.monitor = new Monitor(this);
        try {
            JDABuilder builder = JDABuilder.createDefault(botToken);
            builder.addEventListeners(commandManager);
            builder.addEventListeners(new MessageListeners());
            builder.addEventListeners(new MonitorListeners(this));
            builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            builder.setBulkDeleteSplittingEnabled(false);
            bot = builder.build().awaitReady();
        } catch (LoginException e) {
            logger.error("Der angegebene Bot Token ist nicht gültig!");
        } catch (InterruptedException e) {
            logger.error("Der Bot wurden beim Starten unterbrochen!");
        }
        commandManager.load(this);
        bot.getPresence().setPresence(Activity.watching(bot.getGuilds().size() + " Servern zu."), false);
        bot.getPresence().setPresence(OnlineStatus.ONLINE, false);
    }

    public JDA getBot() {
        return bot;
    }

    public Logger getLogger() {
        return logger;
    }

    public Monitor getMonitor() {
        return monitor;
    }
}
