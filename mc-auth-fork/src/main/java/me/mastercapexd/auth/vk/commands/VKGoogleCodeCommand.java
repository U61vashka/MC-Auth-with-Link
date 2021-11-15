package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.utils.Connector;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class VKGoogleCodeCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKGoogleCodeCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (!receptioner.getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getLegacyMessage("google-disabled"));
			return;
		}
		if (args.length < 2) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKMessages().getLegacyMessage("google-code-not-enough-arguments"));
			return;
		}
		String playerName = args[0];
		if (!isInteger(args[1]) || args[1].length() != 6) {
			sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages().getLegacyMessage("google-need-integer"));
			return;
		}
		Integer enteredCode = Integer.parseInt(args[1]);
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			if (account.getGoogleKey() == null && account.getGoogleKey().isEmpty()) {
				sendMessage(e.getPeer(), receptioner.getConfig().getVKMessages()
						.getLegacyMessage("google-code-account-not-have-google"));
				return;
			}
			if (!Auth.hasGoogleAuthAccount(account.getId())) {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKMessages().getLegacyMessage("google-code-account-not-need-enter"));
				return;
			}
			if (receptioner.getPlugin().getGoogleAuthenticator().authorize(account.getGoogleKey(), enteredCode)) {
				Auth.removeGoogleAuthAccount(account.getId());
				Auth.removeAccount(account.getId());
				ProxiedPlayer proxiedPlayer = account.getIdentifierType().getPlayer(account.getId());
				if (proxiedPlayer != null) {
					sendMessage(e.getPeer(),
							receptioner.getConfig().getVKMessages().getLegacyMessage("google-code-valid"));
					Connector.connectOrKick(proxiedPlayer,
							receptioner.getConfig().findServerInfo(receptioner.getConfig().getGameServers()),
							receptioner.getConfig().getBungeeMessages().getMessage("game-servers-connection-refused"));
				}
			} else {
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKMessages().getLegacyMessage("google-code-not-valid"));
			}
		});
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String getKey() {
		return "google-code";
	}

}