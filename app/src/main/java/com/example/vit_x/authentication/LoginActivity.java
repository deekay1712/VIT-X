package com.example.vit_x.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vit_x.MainActivity;
import com.example.vit_x.R;
import com.example.vit_x.SplashScreenActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginBtn;
    private String email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        //init views
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setEnabled(false);
                validateData();
            }
        });
    }

    private void validateData() {
        email = loginEmail.getText().toString();
        password = loginPassword.getText().toString();

        if(email.isEmpty()){
            loginBtn.setEnabled(true);
            loginEmail.setError("Required");
            loginEmail.requestFocus();
        }else if(password.isEmpty()){
            loginBtn.setEnabled(true);
            loginPassword.setError("Required");
            loginPassword.requestFocus();
        }else {
            loginUser();
        }
    }

    private void loginUser() {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        openSplashScreenActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginBtn.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openSplashScreenActivity() {
        finishAffinity();
        startActivity(new Intent(this, SplashScreenActivity.class));
        finish();
    }
}