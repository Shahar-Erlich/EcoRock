package com.ecorock.game.Ui.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;

import com.ecorock.game.Repository.repository;
import com.ecorock.game.User;

public class LoginModule {

    User user;
    Context context;
    repository repository;
    public LoginModule(User u, Context c)
    {
        this.user = u;
        this.context = c;
        repository = new repository(context);
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
//            if(!(etPassword.getText().toString().equals(etPasswordConfirmation.getText().toString())))
//            {
//                etPassword.setError("Password Confirmation does not match");
//                return false;
//            }
        if(repository.checkLogin(etEmail.getText().toString(),etPassword.getText().toString())) {
            etPassword.setText("");
            etEmail.setText("");
            return true;
        }
        return false;
    }
    public void SharedPreferences(String Name, String Email, String Password)
    {
            SharedPreferences sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", Name);
            editor.putString("email", Email);
            editor.putString("password", Password);
            editor.apply();
    }
    public void removeDataSharedPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Main", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("email");
        editor.remove("password");
        editor.apply();
    }
    public User getUser(String Mail){
        return repository.findUserByMail(Mail);
    }
}
