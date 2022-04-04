package me.mastercapexd.auth.proxy;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.title.ProxyTitle;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

public interface ProxyCore extends Castable<ProxyCore> {
	<E> void callEvent(E event);

	ProxyPlayer getPlayer(UUID uniqueId);

	ProxyPlayer getPlayer(String name);

	Logger getLogger();

	ProxyTitle createTitle(String title);

	ProxyBossbar createBossbar(String title);

	Server serverFromName(String serverName);

	void registerListener(ProxyPlugin plugin, Object listener);

	void schedule(ProxyPlugin plugin, Runnable task, long delay, long period, TimeUnit unit);

	void runAsync(Runnable task);
}