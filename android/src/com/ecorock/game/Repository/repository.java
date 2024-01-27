package com.ecorock.game.Repository;

import android.content.Context;

import com.ecorock.game.Database.MyDatabaseHelper;
import com.ecorock.game.User;

public class repository {
    MyDatabaseHelper helper;

    public repository(Context context){
        helper = new MyDatabaseHelper(context);
    }

    public void addUser(User user){
        helper.addUser(user.getName(), user.getMail(), user.getPass());
    }
    public boolean findUser(String mail){
        boolean u = helper.FindUserByMailExists(mail);;
        return u;
    }
    public boolean checkLogin(String mail,String pass){
        return helper.CheckLogIn(mail,pass);
    }
    public User findUserByMail(String mail){
        return helper.FindUserByMail(mail);
    }
    public void deleteAll(){
        helper.deleteAllData();
    }
}
