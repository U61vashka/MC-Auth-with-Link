package me.mastercapexd.auth.config.vk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.annotations.ImportantField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKCommandPath implements ConfigurationHolder {
	@ImportantField
	@ConfigField("main-command")
	private String commandPath = null;
	@ConfigField("aliases")
	private List<String> aliases = new ArrayList<>();

	public VKCommandPath(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	public String getCommandPath() {
		return commandPath;
	}

	public List<String> getAliases() {
		return Collections.unmodifiableList(aliases);
	}

	public String[] getCommandPaths() {
		String[] originalCommandPath = { commandPath };
		return Stream.concat(Arrays.stream(aliases.toArray(new String[0])), Arrays.stream(originalCommandPath))
				.toArray(String[]::new);
	}
}
