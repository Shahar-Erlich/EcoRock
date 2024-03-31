package com.ecorock.game.MainPage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ecorock.game.currentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button btnL,btnS,btnO,btnE,btnR;
    private TextView mail,name;
    private SharedPreferences sharedPreferences;
    private Intent intent;
    private LinearLayout ll;
    private repository repository;
    private  String nameS="",mailS="",passS="";
    private int iconS=1,levelS;
    private FirebaseFirestore db;
    private  MainPage mainPage;
    private ImageView prof;
    private ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == 78){
                        Intent intentR = result.getData();
                        if(intentR!=null){
                            String Nname = intentR.getStringExtra("Nname"),Nmail = intentR.getStringExtra("Nmail"),Npass = intentR.getStringExtra("Npass");
                            int Nprof = intentR.getIntExtra("Nicon",1);
                            name.setText(Nname);
                            mail.setText(Nmail);
                            setProfPic(Nprof);
                            mailS = Nmail;
                            nameS = Nname;
                            passS = Npass;
                            iconS = Nprof;
                        }
                    }
                }
            }
    );

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
        db=FirebaseFirestore.getInstance();
        mainPage = (MainPage)requireActivity();

        intent = getActivity().getIntent();
        repository = new repository(getActivity().getBaseContext());
        if(intent.hasExtra("username")){
            nameS=intent.getStringExtra("username");
            mailS=intent.getStringExtra("email");
            passS=intent.getStringExtra("password");
            iconS =intent.getIntExtra("prof",1);
            levelS =intent.getIntExtra("level",-1);
            currentUser.setMail(mailS);
            currentUser.setName(nameS);
            currentUser.setPassword(passS);
            currentUser.setIcon(iconS);
            currentUser.setLevel(levelS);
        }
        else if(currentUser.getName().equals("")&&currentUser.getMail().equals("")){
            sharedPreferences = getActivity().getSharedPreferences("Main", Context.MODE_PRIVATE);
             nameS =(sharedPreferences.getString("username",""));
             mailS =(sharedPreferences.getString("email",""));
             passS =(sharedPreferences.getString("password",""));
             iconS =(sharedPreferences.getInt("prof",1));
             levelS = (sharedPreferences.getInt("level",-1));
            currentUser.setMail(mailS);
            currentUser.setName(nameS);
            currentUser.setPassword(passS);
            currentUser.setIcon(iconS);
            currentUser.setLevel(levelS);
        }
        else{
            nameS=currentUser.getName();
            mailS=currentUser.getMail();
            passS=currentUser.getPassword();
            iconS =currentUser.getIcon();
            levelS =currentUser.getLevel();
        }

        if (!nameS.equals("")) {
            View view2 = inflater.inflate(R.layout.fragment_account_loggedin, container, false);
            name = view2.findViewById(R.id.nameAcO);
            mail = view2.findViewById(R.id.MailAcO);
            prof = view2.findViewById(R.id.profilePic);
            name.setText(nameS);
            mail.setText(mailS);
            setProfPic(iconS);
            btnO= view2.findViewById(R.id.LoginBtnO);
            btnE= view2.findViewById(R.id.EditProfBtn);
            btnR= view2.findViewById(R.id.RemoveProfBtn);
            btnE.setOnClickListener(this);
            btnR.setOnClickListener(this);
            btnO.setOnClickListener(this);
            mainPage.setLoggedIn(true);
            return view2;
        }

        return view;
    }
    public void setProfPic(int ind){
        switch (ind){
            case 1:
                prof.setImageResource(R.drawable.defaulticondrawing);
                break;
            case 2:
                prof.setImageResource(R.drawable.dog);
                break;
            case 3:
                prof.setImageResource(R.drawable.default_icon);
                break;
            case 4:
                prof.setImageResource(R.drawable.ic_launcher);
                break;
        }
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
            mainPage.setLoggedIn(false);
            mainPage.setUserLevel(0);
            currentUser.setMail("");
            currentUser.setName("");
            currentUser.setPassword("");
            currentUser.setIcon(0);
            currentUser.setLevel(0);
            startActivity(new Intent(getActivity().getBaseContext(), HomeScreen.class));
        }
        if(v == btnE){
            Intent intent = new Intent(requireActivity(),UpdateActivity.class);
            intent.putExtra("name",nameS);
            intent.putExtra("mail",mailS);
            intent.putExtra("pass",passS);
            intent.putExtra("prof",iconS);
            activityLauncher.launch(intent);
        }
        if(v==btnR){
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get("email").toString().equals(mailS)) {
                                String id = document.getId();
                                // Add a new document with a generated ID
                                db.collection("users")
                                        .document(id).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getActivity().getBaseContext(), "lmao", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                        }
                    }
                }
            });
            startActivity(new Intent(getActivity().getBaseContext(),MainPage.class));
        }
    }
}