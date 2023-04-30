package com.jeff_media.playerdataapi.events;

import com.jeff_media.playerdataapi.data.GlobalPlayer;

public class GlobalPlayerJoinEvent extends GlobalPlayerEvent {
    public GlobalPlayerJoinEvent(GlobalPlayer globalPlayer) {
        super(globalPlayer);
    }

    @Override
    public String toString() {
        return "GlobalPlayerJoinEvent{" +
                "globalPlayer=" + globalPlayer +
                '}';
    }
}
