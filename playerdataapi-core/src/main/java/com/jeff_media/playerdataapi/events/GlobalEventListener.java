package com.jeff_media.playerdataapi.events;

import com.jeff_media.playerdataapi.PlayerDataAPI;
import com.jeff_media.playerdataapi.util.ReflectionUtils;
import redis.clients.jedis.JedisPubSub;

public class GlobalEventListener extends JedisPubSub {

    private final PlayerDataAPI api;
    public GlobalEventListener(PlayerDataAPI api) {
        this.api = api;
    }

    @Override
    public void onMessage(String channel, String message) {
        String[] split = message.split("\\|", 3);
        String originServer = split[0];
        Class<? extends GlobalEvent> eventClazz = (Class<? extends GlobalEvent>) ReflectionUtils.getClass(split[1]);
        String json = split[1];
        System.out.println("Global event received from " + originServer + ": " + json);
        if(originServer.equals(api.getProvider().getServerName())) {
            System.out.println("Ignoring event because it was sent by this server");
            return;
        }
        System.out.println("Calling event on this server now...");
        GlobalEvent event = PlayerDataAPI.gson.fromJson(json, eventClazz);
        api.getGlobalEventManager().callEvent(event);
    }
}