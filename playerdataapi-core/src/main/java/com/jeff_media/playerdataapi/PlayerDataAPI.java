package com.jeff_media.playerdataapi;

import com.google.gson.Gson;
import com.jeff_media.playerdataapi.events.GlobalEventManager;

import java.util.logging.Logger;

/**
 * <b>Entry point</b> - cast the plugins instance to this, then get the DataProvider from it.
 */
public interface PlayerDataAPI {

    Gson gson = new Gson();

    DataProvider getProvider();
    Logger getLogger();
//    void registerGlobalEvents(Object listener);

    GlobalEventManager getGlobalEventManager();

    default Gson getGson() {
        return gson;
    }

}
