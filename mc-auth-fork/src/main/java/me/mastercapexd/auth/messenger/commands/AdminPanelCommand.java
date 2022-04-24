package me.mastercapexd.auth.messenger.commands;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.orphan.OrphanCommand;

public class AdminPanelCommand implements OrphanCommand {
	@Default
	public void adminPanelMenu(LinkCommandActorWrapper actorWrapper,LinkType linkType,Account account) {
		IKeyboard adminPanelKeyboard = linkType.getSettings().getKeyboards().createKeyboard("admin-panel");
		actorWrapper.send(linkType.newMessageBuilder().keyboard(adminPanelKeyboard).rawContent(linkType.getLinkMessages().getMessage("admin-panel")).build());
	}
}
