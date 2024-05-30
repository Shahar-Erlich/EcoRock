package com.ecorock.game.Ui.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ecorock.game.User;

public class LoginModule {

    User user;
    Context context;
    public LoginModule(User u, Context c)
    {
        this.user = u;
        this.context = c;
    }
    public boolean Check_User(EditText etEmail, EditText etPassword)
    {
        // email validity checkups
        if(etEmail.getText().toString().indexOf("@") <= 1)
        {
            etEmail.setError("invalid email (x@)");
            return false;
        }
        if(etEmail.getText().toString().indexOf("@") != etEmail.getText().toString().lastIndexOf("@"))
        {
            etEmail.setError("invalid email (@@)");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") - etEmail.getText().toString().indexOf("@") <= 3)
        {
            etEmail.setError("invalid email (.@)");
            return false;
        }
        if(etEmail.getText().toString().indexOf(".") != etEmail.getText().toString().lastIndexOf("."))
        {
            etEmail.setError("invalid email (..)");
            return false;
        }
        if(!(etEmail.getText().toString().contains(".com")) && !(etEmail.getText().toString().contains(".co.")))
        {
            etEmail.setError("invalid email (.com/.co)");
            return false;
        }
        //password validity checkups
        if(etPassword.getText().toString().equals(""))
        {
            etPassword.setError("Fill Password");
            return false;
        }
        if(etPassword.getText().toString().length() < 3)
        {
            etPassword.setError("Password isn't strong enough");
            return false;
        }
        return false;
    }
    public void SharedPreferences(String Name, String Email, String Password,int prof,int level)
    {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", Name);
            editor.putString("email", Email);
            editor.putString("password", Password);
            editor.putInt("prof",prof);
            editor.putInt("level",level);
            editor.apply();
    }
    public void removeDataSharedPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("email");
        editor.remove("password");
        editor.remove("prof");
        editor.remove("level");
        editor.apply();
    }
}
