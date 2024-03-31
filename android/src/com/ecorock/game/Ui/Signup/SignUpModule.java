package com.ecorock.game.Ui.Signup;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.ecorock.game.User;
import com.ecorock.game.Repository.repository;

public class SignUpModule {
        User user;
        Context context;
        public SignUpModule(User u, Context c)
        {
            this.user = u;
            this.context = c;
        }
        public boolean Check_User(EditText etUser, EditText etEmail, EditText etPassword)
        {
            repository repository = new repository(context);
            // username validity checkups
            if(etUser.getText().toString().isEmpty())
            {
                etUser.setError("Fill Username");
                return false;
            }
            if(etUser.getText().toString().length() < 3)
            {
                etUser.setError("Username must be over 3 characters");
                return false;
            }

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


            return true;
        }
}
