package com.example.vit_x.authentication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vit_x.MainActivity;
import com.example.vit_x.R;
import com.example.vit_x.SplashScreenActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, userEmail, userLinkedinUrl, userPassOutYear, userPassword, userConfirmPassword, userRegistrationNumber;
    private AutoCompleteTextView userProgramme;
    private ImageView userImageView;
    private Button userSelectImageBtn, userRegistrationBtn;
    private String name, email, profileImageUrl, linkedinUrl, programme, passOutYear, password, confirmPassword, uid, registrationNo;

    ActivityResultLauncher<String> takeImage;
    private Bitmap bitmap;

    private FirebaseAuth auth;
    private DatabaseReference dbReference;
    private DatabaseReference dbRef;
    private StorageReference sReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();
        sReference = FirebaseStorage.getInstance().getReference();

        String[] programmes = new String[]{"B.Arch", "BBA", "B.Tech Aerospace Engineering", "B.Tech Bioengineering", "B.Tech Computer Science & Engineering", "B.Tech Electronics & Communication Engineering", "B.Tech Mechanical Engineering", "M.Tech Artificial Intelligence", "M.Tech Computer Science & Engineering", "M.Sc. Biotechnology", "M.Tech VLSI Design", "MBA", "MCA", "Ph.D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.programmes_drop_down, programmes);

        userImageView = findViewById(R.id.registerImageView);
        userSelectImageBtn = findViewById(R.id.registerSelectImageBtn);
        userName = findViewById(R.id.registerName);
        userEmail = findViewById(R.id.registerEmail);
        userRegistrationNumber = findViewById(R.id.registerRegistrationNo);
        userLinkedinUrl = findViewById(R.id.registerLinkedinUrl);
        userPassOutYear = findViewById(R.id.registerPassOutYear);
        userPassword = findViewById(R.id.registerPassword);
        userConfirmPassword = findViewById(R.id.registerConfirmPassword);
        userRegistrationBtn = findViewById(R.id.loginBtn);

        userProgramme = findViewById(R.id.programmeDropDown);
        userProgramme.setAdapter(adapter);

        takeImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        userImageView.setImageBitmap(bitmap);
                    }
                }
        );

        userSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage.launch("image/*");
            }
        });


        userRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRegistrationBtn.setEnabled(false);
                validateData();
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
        finishAffinity();
    }

    private void validateData() {
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        linkedinUrl = userLinkedinUrl.getText().toString();
        programme = userProgramme.getText().toString();
        passOutYear = userPassOutYear.getText().toString();
        password = userPassword.getText().toString();
        confirmPassword = userConfirmPassword.getText().toString();
        registrationNo = userRegistrationNumber.getText().toString();

        if(bitmap==null){
            userRegistrationBtn.setEnabled(true);
            Toast.makeText(RegisterActivity.this, "Please add Image", Toast.LENGTH_SHORT).show();
        }
        else if(name.isEmpty()){
            userRegistrationBtn.setEnabled(true);
            userName.setError("Required");
            userName.requestFocus();
        }else if(registrationNo.isEmpty()){
            userRegistrationBtn.setEnabled(true);
            userRegistrationNumber.setError("Required");
            userRegistrationNumber.requestFocus();
        }else if(email.isEmpty()){
            userRegistrationBtn.setEnabled(true);
            userEmail.setError("Required");
            userEmail.requestFocus();
        }else if(linkedinUrl.isEmpty()){
            userRegistrationBtn.setEnabled(true);
            userLinkedinUrl.setError("Required");
            userLinkedinUrl.requestFocus();
        }else if(programme.isEmpty()) {
            userRegistrationBtn.setEnabled(true);
            userProgramme.setError("Required");
            userProgramme.requestFocus();
        }else if(passOutYear.isEmpty()){
            userRegistrationBtn.setEnabled(true);
            userPassOutYear.setError("Required");
            userPassOutYear.requestFocus();
        }else if(password.isEmpty()){
            userRegistrationBtn.setEnabled(true);
            userPassword.setError("Required");
            userPassword.requestFocus();
        }else if(!password.equals(confirmPassword)){
            userRegistrationBtn.setEnabled(true);
            Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show();
            userConfirmPassword.requestFocus();
        }else {
            createUser();
        }
    }

    private void createUser() {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = auth.getCurrentUser();
                        if (currentUser != null) {
                            uid = currentUser.getUid();
                        }else {
                            uid = "NULL_"+UUID.randomUUID();
                        }
                        uploadUserImage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userRegistrationBtn.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadUserImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();
        
        final StorageReference filePath;
        filePath = sReference.child("Users").child(UUID.randomUUID()+".jpg");
        
        final UploadTask uploadTask = filePath.putBytes(finalImage);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileImageUrl = String.valueOf(uri);
                        uploadUserData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadUserData() {
        dbRef = dbReference.child("Users");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        String createdAt = formatter.format(calendar.getTime());

        HashMap<String, String> user = new HashMap<>();
        user.put("uid", uid);
        user.put("name", name);
        user.put("registrationNo", registrationNo);
        user.put("email", email);
        user.put("linkedinUrl", linkedinUrl);
        user.put("programme", programme);
        user.put("passOutYear", passOutYear);
        user.put("imageUrl", profileImageUrl);
        user.put("isVerified", "false");
        user.put("createdAt", createdAt);

        dbRef.child(uid).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        userRegistrationBtn.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        openSplashScreenActivity();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        userRegistrationBtn.setEnabled(true);
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}