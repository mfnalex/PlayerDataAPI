package com.jeff_media.playerdataapi.events;

import com.jeff_media.playerdataapi.data.GlobalPlayer;

public abstract class GlobalPlayerEvent extends GlobalEvent {

    final GlobalPlayer globalPlayer;

    public GlobalPlayerEvent(GlobalPlayer globalPlayer) {
        this.globalPlayer = globalPlayer;
    }

    public GlobalPlayer getPlayer() {
        return globalPlayer;
    }

}
