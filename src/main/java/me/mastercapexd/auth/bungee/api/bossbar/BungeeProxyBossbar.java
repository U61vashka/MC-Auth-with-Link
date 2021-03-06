package me.mastercapexd.auth.bungee.api.bossbar;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Sets;

import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.md_5.bungee.protocol.packet.BossBar;

public class BungeeProxyBossbar extends ProxyBossbar {

    private static final AtomicInteger barID = new AtomicInteger(1);
    private final Set<ProxyPlayer> players = Sets.newHashSet();
    private final UUID uuid;

    public BungeeProxyBossbar(String title) {
        this.uuid = UUID.nameUUIDFromBytes(("BBB:" + barID.getAndIncrement()).getBytes(StandardCharsets.UTF_8));
        this.title(ComponentSerializer.toString(new TextComponent(this.title)));
    }

    public BungeeProxyBossbar(BaseComponent[] titleComponent) {
        this.uuid = UUID.nameUUIDFromBytes(("BBB:" + barID.getAndIncrement()).getBytes(StandardCharsets.UTF_8));
        this.title(ComponentSerializer.toString(titleComponent));
    }

    @Override
    public ProxyBossbar removeAll() {
        this.players.forEach(this::remove);
        this.players.clear();
        return this;
    }

    @Override
    public ProxyBossbar send(ProxyPlayer... viewers) {
        BossBar bossBar = getAddPacket();
        for (ProxyPlayer player : viewers) {
            player.as(BungeeProxyPlayer.class).getBungeePlayer().unsafe().sendPacket(bossBar);
            players.add(player);
        }
        return this;
    }

    @Override
    public ProxyBossbar remove(ProxyPlayer... viewers) {
        BossBar bossBar = getRemovePacket();
        for (ProxyPlayer player : viewers) {
            player.as(BungeeProxyPlayer.class).getBungeePlayer().unsafe().sendPacket(bossBar);
            players.remove(player);
        }
        return this;
    }

    @Override
    public ProxyBossbar update() {
        BossBar bossBar = getAddPacket();
        for (ProxyPlayer player : players)
            player.as(BungeeProxyPlayer.class).getBungeePlayer().unsafe().sendPacket(bossBar);
        return this;
    }

    @Override
    public Collection<ProxyPlayer> players() {
        return Collections.unmodifiableCollection(players);
    }

    private BossBar getAddPacket() {
        BossBar packet = new BossBar(uuid, 0);

        packet.setTitle(title); // We send title in json
        packet.setColor(this.color.ordinal());
        packet.setDivision(this.segmentStyle.ordinal());
        packet.setHealth(this.progress);
        return packet;
    }

    private BossBar getRemovePacket() {
        return new BossBar(uuid, 1);
    }

}
