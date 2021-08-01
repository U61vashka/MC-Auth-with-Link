package me.mastercapexd.auth;

import java.util.List;
import java.util.regex.Pattern;

import me.mastercapexd.auth.config.BungeeMessages;
import me.mastercapexd.auth.config.VKButtonLabels;
import me.mastercapexd.auth.config.VKMessages;
import me.mastercapexd.auth.objects.Server;
import me.mastercapexd.auth.objects.StorageDataSettings;
import me.mastercapexd.auth.storage.StorageType;
import me.mastercapexd.auth.vk.settings.VKSettings;
import net.md_5.bungee.api.config.ServerInfo;

public interface PluginConfig {

	StorageDataSettings getStorageDataSettings();
	
	VKSettings getVKSettings();
	
	IdentifierType getActiveIdentifierType();
	
	boolean isNameCaseCheckEnabled();
	
	HashType getActiveHashType();
	
	StorageType getStorageType();
	
	Pattern getNamePattern();
	
	Pattern getPasswordPattern();
	
	List<Server> getAuthServers();
	
	List<Server> getGameServers();
	
	boolean isPasswordConfirmationEnabled();
	
	int getPasswordMinLength();
	
	int getPasswordMaxLength();
	
	int getPasswordAttempts();
	
	long getSessionDurability();
	
	long getAuthTime();
	
	BungeeMessages getBungeeMessages();
	
	VKMessages getVKMessages();
	
	VKButtonLabels getVKButtonLabels();
	
	ServerInfo findServerInfo(List<Server> servers);
}