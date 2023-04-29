package com.jeff_media.playerdataapi;

public class UnitTest {

    DataProvider provider = new DataProvider("jdbc:mysql://127.0.0.1:3306/test", "test","test", "localhost", 6379);

    public DataProvider getProvider() {
        return provider;
    }
}
