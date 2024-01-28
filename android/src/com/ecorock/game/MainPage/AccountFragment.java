package com.ecorock.game.MainPage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginModule;
import com.ecorock.game.Ui.Login.LoginPage;
import com.ecorock.game.Ui.MainPage.HomeScreen;
import com.ecorock.game.Ui.Signup.SignupPage;
import com.ecorock.game.User;
import com.ecorock.game.Repository.repository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnL,btnS,btnO,btnE,btnR;
    private TextView mail,name;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private LinearLayout ll;
    private repository repository;
    private  String nameS="",mailS="",passS="";

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_loggeout, container, false);
        btnL = view.findViewById(R.id.LoginBtn);
        btnS = view.findViewById(R.id.SignupBtn);
        ll = view.findViewById(R.id.ll);
        btnL.setOnClickListener(this);
        btnS.setOnClickListener(this);

        intent = getActivity().getIntent();
        repository = new repository(getActivity().getBaseContext());
        if(intent.hasExtra("username")){
            nameS=intent.getStringExtra("username");
            mailS=intent.getStringExtra("email");
            passS=intent.getStringExtra("password");
        }
        else{
            sharedPreferences = getActivity().getSharedPreferences("Main", Context.MODE_PRIVATE);
//            name.setText(sharedPreferences.getString("username",""));
//            mail.setText(sharedPreferences.getString("email",""));
             nameS =(sharedPreferences.getString("username",""));
             mailS =(sharedPreferences.getString("email",""));
             passS =(sharedPreferences.getString("password",""));
        }

        if (!nameS.equals("")) {
            View view2 = inflater.inflate(R.layout.fragment_account_loggedin, container, false);
            name = view2.findViewById(R.id.nameAcO);
            mail = view2.findViewById(R.id.MailAcO);
            name.setText(nameS);
            mail.setText(mailS);
            btnO= view2.findViewById(R.id.LoginBtnO);
            btnE= view2.findViewById(R.id.EditProfBtn);
            btnR= view2.findViewById(R.id.RemoveProfBtn);
            btnE.setOnClickListener(this);
            btnR.setOnClickListener(this);
            btnO.setOnClickListener(this);
            return view2;
        }


        return view;
    }

    @Override
    public void onClick(View v) {
        LoginModule loginModule = new LoginModule(new User("","",""),getActivity().getBaseContext());
        if(v == btnL){
            startActivity(new Intent(requireContext(), LoginPage.class));
        }
        if(v==btnS){
            startActivity(new Intent(requireContext(), SignupPage.class));
        }
        if(v == btnO){
            loginModule.removeDataSharedPreferences();
            startActivity(new Intent(getActivity().getBaseContext(), HomeScreen.class));
        }
        if(v == btnE){
        UpdateDialog(nameS,mailS,passS);
        }
        if(v==btnR){
            repository.deleteUser(mailS);
            loginModule.removeDataSharedPreferences();
            startActivity(new Intent(getActivity().getBaseContext(),MainPage.class));
        }
    }
    private void UpdateDialog(String name,String mail,String pass) {
        Dialog dialog=new Dialog(requireActivity());
        dialog.setContentView(R.layout.update_dialog);
        EditText upname,upmail,uppass;
        Button btnClose,btnUpdate;
        btnUpdate = dialog.findViewById(R.id.btnUpdate);
        btnClose= dialog.findViewById(R.id.btnCancel);
        upname = dialog.findViewById(R.id.editTextName);
        upmail = dialog.findViewById(R.id.editTextEmail);
        uppass = dialog.findViewById(R.id.editTextPassword);
        upname.setText(name);
        upmail.setText(mail);
        uppass.setText(pass);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailU,nameU,passU;
                mailU = upmail.getText().toString();
                nameU = upname.getText().toString();
                passU = uppass.getText().toString();
                LoginModule loginModule = new LoginModule(new User("","",""),getActivity().getBaseContext());
                loginModule.SharedPreferences(nameU,mailU,passU);
            repository.updateUser(mail,new User(nameU,mailU,passU));
            dialog.dismiss();
            startActivity(new Intent(getActivity().getBaseContext(),MainPage.class));
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
    }
}