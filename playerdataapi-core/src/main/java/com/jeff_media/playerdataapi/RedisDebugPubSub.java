package com.jeff_media.playerdataapi;

import redis.clients.jedis.JedisPubSub;

public class RedisDebugPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("Received Redis message: " + "channel=" + channel + ", message=" + message);
    }
}
