package com.jeff_media.playerdataapi.util;

import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

    private static final Map<String,Class<?>> CLASSES_BY_NAME = new HashMap<>();

    public static Class<?> getClass(String name){
        System.out.println("Trying to get class " + name);
        if(CLASSES_BY_NAME.containsKey(name)) {
            return CLASSES_BY_NAME.get(name);
        }
        Class<?> clazz;
        try {
            clazz = Class.forName(name);
            CLASSES_BY_NAME.put(name,clazz);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
