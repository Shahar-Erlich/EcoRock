package com.ecorock.game.MainPage;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginPage;
import com.ecorock.game.Ui.Signup.SignupPage;

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
    private Button btnL,btnS;
    private TextView mail,name;
    private SharedPreferences sharedPreferences;
    private Intent intent;

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
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        btnL = view.findViewById(R.id.LoginBtn);
        btnS = view.findViewById(R.id.SignupBtn);
        name = view.findViewById(R.id.nameAc);
        mail = view.findViewById(R.id.MailAc);
        btnL.setOnClickListener(this);
        btnS.setOnClickListener(this);
        intent = getActivity().getIntent();
        if(intent.hasExtra("username")){
            name.setText(intent.getStringExtra("username"));
            mail.setText(intent.getStringExtra("email"));
        }
        else{
            sharedPreferences = getActivity().getSharedPreferences("Main", Context.MODE_PRIVATE);
            name.setText(sharedPreferences.getString("username",""));
            mail.setText(sharedPreferences.getString("email",""));
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == btnL){
            startActivity(new Intent(requireContext(), LoginPage.class));
        }
        if(v==btnS){
            startActivity(new Intent(requireContext(), SignupPage.class));
        }
    }
}