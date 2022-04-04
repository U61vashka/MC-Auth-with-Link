package me.mastercapexd.auth.proxy.server;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public interface Server extends Castable<Server> {
	String getServerName();
	
	void sendPlayer(ProxyPlayer... players);
}
