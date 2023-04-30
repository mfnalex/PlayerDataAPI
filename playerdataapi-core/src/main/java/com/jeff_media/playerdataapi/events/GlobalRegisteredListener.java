package com.jeff_media.playerdataapi.events;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public record GlobalRegisteredListener(Object instance, Method method, byte priority) implements Comparable<GlobalRegisteredListener> {
    @Override
    public int compareTo(@NotNull GlobalRegisteredListener o) {
        return Integer.compare(priority, o.priority());
    }
}



