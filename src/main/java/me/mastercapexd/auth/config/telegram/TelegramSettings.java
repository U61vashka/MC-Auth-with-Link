package me.mastercapexd.auth.config.telegram;

import java.util.List;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.telegram.TelegramMessages;
import me.mastercapexd.auth.config.messenger.DefaultCommandPaths;
import me.mastercapexd.auth.config.messenger.DefaultConfirmationSettings;
import me.mastercapexd.auth.config.messenger.DefaultEnterSettings;
import me.mastercapexd.auth.config.messenger.DefaultMessengerCustomCommands;
import me.mastercapexd.auth.config.messenger.DefaultRestoreSettings;
import me.mastercapexd.auth.config.messenger.MessengerCustomCommands;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class TelegramSettings implements ConfigurationHolder, MessengerSettings {
    @ConfigField("enabled")
    private boolean enabled = false;
    @ConfigField("token")
    private String token;
    @ConfigField("confirmation")
    private DefaultConfirmationSettings confirmationSettings;
    @ConfigField("restore")
    private DefaultRestoreSettings restoreSettings;
    @ConfigField("enter")
    private DefaultEnterSettings enterSettings;
    @ConfigField("telegram-commands")
    private DefaultCommandPaths commandPaths;
    @ConfigField("custom-commands")
    private DefaultMessengerCustomCommands commands;
    @ConfigField("max-telegram-link")
    private Integer maxTelegramLinkCount = 0;
    @ConfigField("telegram-messages")
    private TelegramMessages messages;
    @ConfigField("keyboards")
    private TelegramKeyboards keyboards;
    @ConfigField("admin-accounts")
    private List<Number> adminAccounts;

    public TelegramSettings() {
    }

    public TelegramSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
        if (token == null && enabled)
            System.err.println("Telegram bot token not found!");
    }

    public String getBotToken() {
        return token;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public DefaultConfirmationSettings getConfirmationSettings() {
        return confirmationSettings;
    }

    @Override
    public MessengerCustomCommands getCustomCommands() {
        return commands;
    }

    @Override
    public DefaultEnterSettings getEnterSettings() {
        return enterSettings;
    }

    @Override
    public boolean isAdministrator(LinkUserIdentificator identificator) {
        if (identificator == null || !identificator.isNumber())
            return false;
        return adminAccounts.stream().anyMatch(number -> number.longValue() == identificator.asNumber());
    }

    public boolean isAdministrator(long userId) {
        return isAdministrator(new UserNumberIdentificator(userId));
    }

    @Override
    public DefaultRestoreSettings getRestoreSettings() {
        return restoreSettings;
    }

    @Override
    public DefaultCommandPaths getCommandPaths() {
        return commandPaths;
    }

    @Override
    public int getMaxLinkCount() {
        return maxTelegramLinkCount;
    }

    @Override
    public TelegramMessages getMessages() {
        return messages;
    }

    @Override
    public TelegramKeyboards getKeyboards() {
        return keyboards;
    }
}
