package me.mastercapexd.auth.telegram.commands;

import com.ubivashka.lamp.telegram.core.TelegramHandler;

import me.mastercapexd.auth.hooks.TelegramPluginHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import revxrsal.commands.CommandHandler;

public class TelegramCommandRegistry extends MessengerCommandRegistry {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	private static final TelegramPluginHook TELEGRAM_HOOK = PLUGIN.getHook(TelegramPluginHook.class);
	private static final CommandHandler COMMAND_HANDLER = new TelegramHandler(TELEGRAM_HOOK.getTelegramBot());

	public TelegramCommandRegistry() {
		super(COMMAND_HANDLER, TelegramLinkType.getInstance());
		COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class,
				context -> new TelegramCommandActorWrapper(context.actor()));
		registerCommands();

		TELEGRAM_HOOK.getTelegramBot().setUpdatesListener(new TelegramCommandUpdatesListener());
	}
}
