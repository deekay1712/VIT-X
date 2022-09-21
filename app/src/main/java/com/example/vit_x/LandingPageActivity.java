package com.example.vit_x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vit_x.authentication.LoginActivity;
import com.example.vit_x.authentication.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandingPageActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private Button loginBtn;
    private TextView registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        auth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.landingPageLoginBtn);
        registerBtn = findViewById(R.id.landingPageRegisterBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginActivity();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            openSplashScreenActivity();
        }
    }

    private void openSplashScreenActivity() {
        startActivity(new Intent(this, SplashScreenActivity.class));
        finish();
    }

    private void openRegisterActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void openLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}