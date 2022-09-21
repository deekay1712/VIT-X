package com.example.vit_x;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");

        if(auth.getCurrentUser()!=null){
            dbReference.child(auth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("isVerified").getValue().equals("true"))
                                openMainActivity();
                            else
                                openNotVerifiedActivity();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    private void openNotVerifiedActivity() {
        startActivity(new Intent(this, NotVerifiedActivity.class));
        finish();
    }

    private void openMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}