package com.ecorock.game.Ui.Signup;

import android.content.Context;

import com.ecorock.game.Repository.repository;

public class UserModule {
    private repository repository;

    public UserModule(Context context){
        repository = new repository(context);
    }


}
