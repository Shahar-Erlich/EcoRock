package com.ecorock.game.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import com.ecorock.game.R;
import com.ecorock.game.Ui.Login.LoginModule;
import com.ecorock.game.User;
import com.ecorock.game.currentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity {

    private ViewPager viewPager;
    private static boolean LoggedIn,firstTime;
    private static int Level=0;
    private int levelI,score;
    private static FirebaseFirestore db;
    private BottomNavigationView bottomNavigation;
    LoginModule loginModule;
    SharedPreferences sharedPreferences;
    String mailS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(getWindow(),getWindow().getDecorView());
        windowInsetsControllerCompat.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.systemBars());
        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setItemIconTintList(null);
        Intent intent = getIntent();
        loginModule = new LoginModule(new User("","",""),this);
        if(intent.hasExtra("level")) {
            Level = intent.getIntExtra("level", -1);
            levelI = intent.getIntExtra("levelI",-1);
            score = intent.getIntExtra("score",0);
        }
        sharedPreferences = getSharedPreferences("Main", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("email","").equals("")&&currentUser.getMail()=="") {
            currentUser.setMail(sharedPreferences.getString("email", ""));
        }
        mailS = currentUser.getMail();
        db=FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get("email").toString().equals(mailS)) {
                            String id = document.getId();
                            firstTime = Boolean.parseBoolean(document.getData().get("firstTime").toString());
                            Map<String, Object> user = new HashMap<>();
                            if(score!=-1&&levelI!=-1){
                                if(document.getData().get("levelScores." + String.valueOf(levelI))!=null) {
                                    if(Integer.parseInt(document.getData().get("levelScores." + String.valueOf(levelI)).toString())<score) {
                                        user.put("levelScores." + String.valueOf(levelI), score);
                                    }
                                }
                                else{
                                    user.put("levelScores." + String.valueOf(levelI), score);
                                }
                            }
                            if (Integer.parseInt(document.getData().get("level").toString()) < Level) {
                                user.put("level", Level);
                            }
                            else{
                                Level = Integer.parseInt(document.getData().get("level").toString());
                            }
                                // Add a new document with a generated ID
                                db.collection("users")
                                        .document(id).update(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
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
        // Attach the adapter to the ViewPager
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                // Set the starting fragment to be the middle one
                viewPager.setCurrentItem(1);
                return true;
            }
        });
        viewPager.setPageTransformer(true, new CustomPageTransformer());

        // Set up BottomNavigationView to handle item selection
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_fragment1) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_fragment2) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.navigation_fragment3) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });

        // Set up ViewPager to sync with BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void navigateToFragment(int position) {
        viewPager.setCurrentItem(position);
    }
    public void setLoggedIn(boolean l){
        LoggedIn=l;
    }
    public int getUserLevel(){
        return Level;
    }
    public boolean getFirstTime(){
        return firstTime;
    }
    public static void setFirstTime(boolean b){
        firstTime = b;
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get("email").toString().equals(currentUser.getMail())) {
                            String id = document.getId();
                            firstTime = Boolean.parseBoolean(document.getData().get("firstTime").toString());
                            if (Boolean.parseBoolean(document.getData().get("firstTime").toString()) !=b) {
                                Map<String, Object> user = new HashMap<>();
                                user.put("firstTime", b);
                                // Add a new document with a generated ID
                                db.collection("users")
                                        .document(id).update(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        });
                            }
                            else{
                                firstTime = Boolean.parseBoolean(document.getData().get("firstTime").toString());
                            }
                        }
                    }
                }
            }
        });
    }
    public void setUserLevel(int l){
       Level = l;
    }
    public boolean getLoggedIn(){return LoggedIn;}
}
