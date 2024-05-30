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
    private  String nameS="",mailS="",passS="";
    private int iconS=1,levelS;
    private FirebaseFirestore db;
    private  MainPage mainPage;
    private ImageView prof;
    // Register an activity result launcher for handling the result of starting an activity
    private ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            // Use the StartActivityForResult contract to start an activity for result
            new ActivityResultContracts.StartActivityForResult(),
            // Define the callback to handle the result of the activity
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Check if the result code matches the expected value (78 in this case)
                    if (result.getResultCode() == 78) {
                        // Get the intent that contains the result data
                        Intent intentR = result.getData();
                        if(intentR!=null){
                            // Retrieve the new user data from the intent
                            String Nname = intentR.getStringExtra("Nname");
                            String Nmail = intentR.getStringExtra("Nmail");
                            String Npass = intentR.getStringExtra("Npass");
                            int Nprof = intentR.getIntExtra("Nicon", 1);

                            // Update the UI elements with the new user data
                            name.setText(Nname);
                            mail.setText(Nmail);
                            setProfPic(Nprof);

                            // Update the local variables with the new user data
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
        // Inflate the layout for the logged out account fragment
        View view = inflater.inflate(R.layout.fragment_account_loggeout, container, false);

        // Find and assign buttons and layout elements from the view
        btnL = view.findViewById(R.id.LoginBtn);
        btnS = view.findViewById(R.id.SignupBtn);
        ll = view.findViewById(R.id.ll);

        // Set click listeners for the buttons
        btnL.setOnClickListener(this);
        btnS.setOnClickListener(this);

        // Initialize the Firestore database instance
        db = FirebaseFirestore.getInstance();
        // Get the main activity reference
        mainPage = (MainPage) requireActivity();

        // Retrieve the intent that started this activity
        intent = getActivity().getIntent();

        // Check if the intent has extra data
        if (intent.hasExtra("username")) {
            // Retrieve data from the intent extras
            nameS = intent.getStringExtra("username");
            mailS = intent.getStringExtra("email");
            passS = intent.getStringExtra("password");
            iconS = intent.getIntExtra("prof", 1);
            levelS = intent.getIntExtra("level", -1);

            // Set the current user's data
            currentUser.setMail(mailS);
            currentUser.setName(nameS);
            currentUser.setPassword(passS);
            currentUser.setIcon(iconS);
            currentUser.setLevel(levelS);
        }

        // Check if the current user's data is empty
        else if (currentUser.getName().equals("") && currentUser.getMail().equals("")) {
            // Retrieve data from shared preferences
            sharedPreferences = getActivity().getSharedPreferences("Main", Context.MODE_PRIVATE);
            nameS = sharedPreferences.getString("username", "");
            mailS = sharedPreferences.getString("email", "");
            passS = sharedPreferences.getString("password", "");
            iconS = sharedPreferences.getInt("prof", 1);
            levelS = sharedPreferences.getInt("level", -1);

            // Set the current user's data
            currentUser.setMail(mailS);
            currentUser.setName(nameS);
            currentUser.setPassword(passS);
            currentUser.setIcon(iconS);
            currentUser.setLevel(levelS);
        }

        // If the current user's data is not empty
        else {
            // Set local variables to the current user's data
            nameS = currentUser.getName();
            mailS = currentUser.getMail();
            passS = currentUser.getPassword();
            iconS = currentUser.getIcon();
            levelS = currentUser.getLevel();
        }

        // If the user's name is not empty, inflate the logged-in account fragment
        if (!nameS.equals("")) {
            View view2 = inflater.inflate(R.layout.fragment_account_loggedin, container, false);

            // Find and assign views from the logged-in layout
            name = view2.findViewById(R.id.nameAcO);
            mail = view2.findViewById(R.id.MailAcO);
            prof = view2.findViewById(R.id.profilePic);

            // Set the user's name and email
            name.setText(nameS);
            mail.setText(mailS);
            // Set the profile picture based on the iconS value
            setProfPic(iconS);


            // Find and assign buttons from the logged-in layout
            btnO = view2.findViewById(R.id.LoginBtnO);
            btnE = view2.findViewById(R.id.EditProfBtn);
            btnR = view2.findViewById(R.id.RemoveProfBtn);

            // Set click listeners for the buttons
            btnE.setOnClickListener(this);
            btnR.setOnClickListener(this);
            btnO.setOnClickListener(this);


            // Update the main page to reflect that the user is logged in
            mainPage.setLoggedIn(true);

            // Return the logged-in view
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
                prof.setImageResource(R.drawable.icon_hand);
                break;
            case 3:
                prof.setImageResource(R.drawable.default_icon);
                break;
            case 4:
                prof.setImageResource(R.drawable.yugi_icon);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        // Initialize the LoginModule with a new User and the current context
        LoginModule loginModule = new LoginModule(new User("", "", ""), getActivity().getBaseContext());

        // Check which button was clicked
        if (v == btnL) {
            // Start the LoginPage activity
            startActivity(new Intent(requireContext(), LoginPage.class));
        }

        if (v == btnS) {
            // Start the SignupPage activity
            startActivity(new Intent(requireContext(), SignupPage.class));
        }

        if (v == btnO) {
            // Remove user data from shared preferences
            loginModule.removeDataSharedPreferences();
            // Update the main page to reflect that the user is logged out
            mainPage.setLoggedIn(false);
            mainPage.setUserLevel(0);
            // Clear the current user's data
            currentUser.setMail("");
            currentUser.setName("");
            currentUser.setPassword("");
            currentUser.setIcon(0);
            currentUser.setLevel(0);
            // Start the HomeScreen activity
            startActivity(new Intent(getActivity().getBaseContext(), HomeScreen.class));
        }

        if (v == btnE) {
            // Create an Intent to start the UpdateActivity
            Intent intent = new Intent(requireActivity(), UpdateActivity.class);
            // Put extra data into the Intent
            intent.putExtra("name", nameS);
            intent.putExtra("mail", mailS);
            intent.putExtra("pass", passS);
            intent.putExtra("prof", iconS);
            // Launch the activity with the prepared Intent
            activityLauncher.launch(intent);
        }

        if (v == btnR) {
            // Fetch all documents from the "users" collection in Firestore
            db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    // Check if the task was successful
                    if (task.isSuccessful()) {
                        // Iterate through the fetched documents
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Check if the email matches the specified email
                            if (document.getData().get("email").toString().equals(mailS)) {
                                String id = document.getId();
                                // Delete the document with the matching ID
                                db.collection("users")
                                        .document(id).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // Success callback
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failure callback
                                            }
                                        });
                            }
                        }
                    }
                }
            });
            // Start the MainPage activity
            startActivity(new Intent(getActivity().getBaseContext(), MainPage.class));
        }
    }
}