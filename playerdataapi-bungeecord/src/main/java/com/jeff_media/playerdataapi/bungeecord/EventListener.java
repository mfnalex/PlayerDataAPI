package com.jeff_media.playerdataapi.bungeecord;

import com.jeff_media.playerdataapi.PlayerDataAPI;
import com.jeff_media.playerdataapi.data.GlobalPlayer;
import com.jeff_media.playerdataapi.events.GlobalPlayerJoinEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

    private final PlayerDataAPI api;

    public EventListener(PlayerDataAPI api) {
        this.api = api;
    }

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        System.out.println("Player joined the proxy, broadcasting event now...");
        GlobalPlayerJoinEvent globalPlayerJoinEvent = new GlobalPlayerJoinEvent(new GlobalPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName()));
        api.getGlobalEventManager().callAndBroadcastEvent(globalPlayerJoinEvent);
    }
}
