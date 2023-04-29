package com.jeff_media.playerdataapi.chatcolor;

import com.jeff_media.playerdataapi.DataProvider;
import com.jeff_media.playerdataapi.table.VarCharTable;

public class ChatColorTable extends VarCharTable {
    public ChatColorTable(DataProvider provider) {
        super(provider, "chatcolors");
    }
}
