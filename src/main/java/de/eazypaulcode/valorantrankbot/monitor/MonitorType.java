package de.eazypaulcode.valorantrankbot.monitor;

public enum MonitorType {

    GUILD_ADDED("Bot added", "<:plus:958710647389040650>"),
    GUILD_REMOVED("Bot removed", "<:minus:958710647410032640>"),

    RANK_REQUESTED("Rank requested", ":question:"),
    ;

    private final String description;
    private final String emoji;

    MonitorType(String description, String emoji) {
        this.description = description;
        this.emoji = emoji;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }
}
