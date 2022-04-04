package me.mastercapexd.auth.proxy.player;

import java.net.InetSocketAddress;
import java.util.UUID;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.server.Server;

public interface ProxyPlayer extends Castable<ProxyPlayer> {

	void sendMessage(String message);

	String getNickname();

	UUID getUniqueId();

	InetSocketAddress getRemoteAddress();

	<T> T getRealPlayer();

	default void sendTo(Server server) {
		server.sendPlayer(this);
	}
}