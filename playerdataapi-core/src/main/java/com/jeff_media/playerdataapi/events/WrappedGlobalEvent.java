package com.jeff_media.playerdataapi.events;

public class WrappedGlobalEvent {
    Class<? extends GlobalEvent> eventClazz;
    String json;

    public WrappedGlobalEvent(Class<? extends GlobalEvent> eventClazz, String json) {
        this.eventClazz = eventClazz;
        this.json = json;
    }

    public Class<? extends GlobalEvent> getEventClazz() {
        return eventClazz;
    }

    public String getJson() {
        return json;
    }


}
