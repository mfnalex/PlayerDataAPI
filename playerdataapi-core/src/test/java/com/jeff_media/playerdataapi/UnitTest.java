package com.jeff_media.playerdataapi;

import com.jeff_media.playerdataapi.DataProvider;

public class UnitTest {

    DataProvider provider = new DataProvider("jdbc:mysql://localhost:3306/minecrafttest", "minecrafttest","minecrafttest");

    public DataProvider getProvider() {
        return provider;
    }
}
