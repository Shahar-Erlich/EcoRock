package com.ecorock.game.Repository;

import android.content.Context;

import com.ecorock.game.Database.MyDatabaseHelper;

public class repository {
    MyDatabaseHelper helper;

    public repository(Context context){
        helper = new MyDatabaseHelper(context);
    }
}
