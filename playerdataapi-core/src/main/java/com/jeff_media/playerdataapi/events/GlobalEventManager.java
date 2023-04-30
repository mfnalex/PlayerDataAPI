package com.jeff_media.playerdataapi.events;

import com.jeff_media.playerdataapi.PlayerDataAPI;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

public class GlobalEventManager {

    private final PlayerDataAPI api;
    private final Map<Class<? extends GlobalEvent>, List<GlobalRegisteredListener>> listenerMap = new HashMap<>();


    public GlobalEventManager(PlayerDataAPI api) {
        this.api = api;
    }

    public void registerEvents(Object object) {
        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(GlobalEventHandler.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new IllegalArgumentException("Method " + method.getName() + " has @GlobalEventHandler annotation, but does not have exactly one parameter.");
                }
                Class<?> parameterType = parameterTypes[0];
                if (!GlobalEvent.class.isAssignableFrom(parameterType)) {
                    throw new IllegalArgumentException("Method " + method.getName() + " has @GlobalEventHandler annotation, but its parameter is not a subclass of GlobalEvent.");
                }
                Class<? extends GlobalEvent> eventType = (Class<? extends GlobalEvent>) parameterType;
                byte priority = method.getAnnotation(GlobalEventHandler.class).priority();

                List<GlobalRegisteredListener> listeners = listenerMap.computeIfAbsent(eventType, __ -> new ArrayList<>());
                listeners.add(new GlobalRegisteredListener(object, method, priority));
                Collections.sort(listeners);

            }
        }
    }

    public void broadcastEvent(GlobalEvent event) {
        System.out.println("Publishing event " + event.getClass().getSimpleName() + " to Redis...");
        api.getProvider().redisPublish("global-events", api.getProvider().getServerName() + "|" + event.getClass().getName() + "|" + api.getGson().toJson(event));
    }

    public void callEvent(GlobalEvent event) {
        List<GlobalRegisteredListener> listeners = listenerMap.get(event.getClass());
        if (listeners == null) return;
        for (GlobalRegisteredListener listener : listeners) {
            try {
                System.out.println("Invoking listener " + listener.instance().getClass().getName() + " for event " + event.getClass().getSimpleName()   );
                listener.method().invoke(listener.instance(), event);
            } catch (Exception e) {
                api.getLogger().log(Level.SEVERE, "Could not pass event " + event.getClass().getSimpleName() + " to listener " + listener.instance().getClass().getName(), e);
            }
        }

    }

    public void callAndBroadcastEvent(GlobalEvent event) {
        callEvent(event);
        broadcastEvent(event);
    }


}
