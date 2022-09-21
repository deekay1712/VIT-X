package com.example.vit_x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class NotVerifiedActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified);

        auth = FirebaseAuth.getInstance();
        topAppBar = findViewById(R.id.topAppBar);

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

    private void openLandingPage() {
        startActivity(new Intent(this, LandingPageActivity.class));
        finish();
    }

}