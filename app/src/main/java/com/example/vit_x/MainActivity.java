package com.example.vit_x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.vit_x.authentication.LoginActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private MaterialToolbar topAppBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        navController = Navigation.findNavController(this, R.id.frame_layout);
        topAppBar = findViewById(R.id.topAppBar);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        setSupportActionBar(topAppBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            auth.signOut();
            openLandingPage();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser()==null)
            openLandingPage();
    }

    private void openLandingPage() {
//        finishAffinity();
        Intent intent = new Intent(this, LandingPageActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        startActivity(new Intent(this, LandingPageActivity.class));
        finish();
    }
}