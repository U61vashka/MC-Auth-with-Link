package me.mastercapexd.auth.config.message.proxy;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.message.AbstractMessages;
import me.mastercapexd.auth.config.message.Messages;
import me.mastercapexd.auth.config.message.context.MessageContext;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class ProxyMessages extends AbstractMessages<ProxyComponent> {

	public ProxyMessages(ConfigurationSectionHolder configurationSection) {
		super(configurationSection);
	}

	@Override
	public ProxyComponent getMessage(String key) {
		return ProxyComponent.fromString(getStringMessage(key));
	}

	@Override
	public ProxyComponent getMessage(String key, MessageContext context) {
		return ProxyComponent.fromString(context.apply(getStringMessage(key)));
	}

	@Override
	public String formatString(String message) {
		return ProxyComponent.fromString(message).legacyText();
	}

	@Override
	protected Messages<ProxyComponent> createMessages(ConfigurationSectionHolder configurationSection) {
		return new ProxyMessages(configurationSection);
	}

	@Override
	public ProxyComponent fromText(String text) {
		return ProxyComponent.fromString(text);
	}

}