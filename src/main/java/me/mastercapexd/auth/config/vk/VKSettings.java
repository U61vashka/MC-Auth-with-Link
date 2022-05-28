package me.mastercapexd.auth.config.vk;

import java.util.List;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.message.vk.VKMessages;
import me.mastercapexd.auth.config.messenger.DefaultMessengerCustomCommands;
import me.mastercapexd.auth.config.messenger.MessengerCustomCommands;
import me.mastercapexd.auth.config.messenger.MessengerSettings;
import me.mastercapexd.auth.config.messenger.DefaultEnterSettings;
import me.mastercapexd.auth.config.messenger.DefaultRestoreSettings;
import me.mastercapexd.auth.config.messenger.DefaultConfirmationSettings;
import me.mastercapexd.auth.config.messenger.DefaultCommandPaths;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKSettings implements ConfigurationHolder, MessengerSettings {
	@ConfigField
	private boolean enabled = false;
	@ConfigField("confirmation")
	private DefaultConfirmationSettings confirmationSettings = null;
	@ConfigField("restore")
	private DefaultRestoreSettings restoreSettings = null;
	@ConfigField("enter")
	private DefaultEnterSettings enterSettings = null;
	@ConfigField("vk-commands")
	private DefaultCommandPaths commandPaths = null;
	@ConfigField("custom-commands")
	private DefaultMessengerCustomCommands commands = null;
	@ConfigField("max-vk-link")
	private Integer maxVkLinkCount = 0;
	@ConfigField("vk-messages")
	private VKMessages messages = null;
	@ConfigField("keyboards")
	private VKKeyboards keyboards = null;
	@ConfigField("admin-accounts")
	private List<Integer> adminAccounts = null;

	public VKSettings() {
	}

	public VKSettings(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
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
		return adminAccounts.contains(identificator.asNumber());
	}

	public boolean isAdministrator(int userId) {
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
		return maxVkLinkCount;
	}

	@Override
	public VKMessages getMessages() {
		return messages;
	}

	@Override
	public VKKeyboards getKeyboards() {
		return keyboards;
	}
}