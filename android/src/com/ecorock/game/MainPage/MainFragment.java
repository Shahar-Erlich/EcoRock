package com.ecorock.game.MainPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ecorock.game.AndroidLauncher;
import com.ecorock.game.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button btn;
    private boolean Logged;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Find the play button in the inflated view
        btn = view.findViewById(R.id.playButton);


        // Get the MainPage activity to access its methods and properties
        MainPage mainPage = (MainPage) requireActivity();

        // Set an OnClickListener for the play button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user is logged in
                Logged = mainPage.getLoggedIn();

                if (Logged) {
                    // If logged in, start the AndroidLauncher activity and pass the user level
                    Intent intent = new Intent(getActivity().getBaseContext(), AndroidLauncher.class);
                    intent.putExtra("level", mainPage.getUserLevel());
                    startActivity(intent);
                }
                else{
                    // If not logged in, show a toast message prompting the user to log in first
                    Toast.makeText(requireContext(), "Log in first to play!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}