package com.example.vit_x.ui.addPost;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vit_x.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class AddPostFragment extends Fragment {

    private ImageView addPostImageView;
    private EditText addPostDescription;
    private Button addPostSelectImageBtn, addPostBtn;

    private LinearProgressIndicator lpi;

    ActivityResultLauncher<String> takeImage;
    private Bitmap bitmap;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference dbReference, dbRef;
    private StorageReference sReference;
    private  String imageDownloadUrl="", description, uid, author, authorImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        dbReference = FirebaseDatabase.getInstance().getReference();
        sReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        addPostImageView = view.findViewById(R.id.addPostImageView);
        addPostDescription = view.findViewById(R.id.addPostDescription);
        addPostSelectImageBtn = view.findViewById(R.id.addPostSelectImageBtn);
        addPostBtn = view.findViewById(R.id.addPostBtn);
        lpi = view.findViewById(R.id.progressBar);

        lpi.setVisibility(View.GONE);

        takeImage = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), result);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        addPostImageView.setImageBitmap(bitmap);
                    }
                }
        );

        //get image from gallery on click
        addPostSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               takeImage.launch("image/*");
            }
        });

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lpi.setVisibility(View.VISIBLE);
                addPostBtn.setEnabled(false);
                getAuthorInfo();
            }
        });

        return view;
    }

    private void getAuthorInfo() {
        //get author data
        uid = currentUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.child(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                author = ""+snapshot.child("name").getValue();
                authorImageUrl=""+snapshot.child("imageUrl").getValue();
                validateData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lpi.setVisibility(View.GONE);
                addPostBtn.setEnabled(true);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateData() {
        if(bitmap==null){
            lpi.setVisibility(View.GONE);
            addPostBtn.setEnabled(true);
            Toast.makeText(getContext(), "Please add image", Toast.LENGTH_SHORT).show();
        }else if(addPostDescription.getText().toString().isEmpty()){
            lpi.setVisibility(View.GONE);
            addPostBtn.setEnabled(true);
            addPostDescription.setError("Required");
        }else if(author.isEmpty() || authorImageUrl.isEmpty() || uid.isEmpty()){
            lpi.setVisibility(View.GONE);
            addPostBtn.setEnabled(true);
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        else {
            uploadImage();
        }
    }

    private void uploadImage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalImage = baos.toByteArray();

        final StorageReference filepath;
        filepath = sReference.child("Posts").child(UUID.randomUUID()+".jpg");

        final UploadTask uploadTask = filepath.putBytes(finalImage);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageDownloadUrl = String.valueOf(uri);
                        uploadData();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lpi.setVisibility(View.GONE);
                addPostBtn.setEnabled(true);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        //get key or PID
        DatabaseReference dbRef = dbReference.child("Posts");
        final String pid = dbRef.push().getKey();

        description = addPostDescription.getText().toString();

        //get date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        String postedAt = formatter.format(calendar.getTime());

        //upload data
        HashMap<String, String> post = new HashMap<>();
        post.put("pid", pid);
        post.put("uid", uid);
        post.put("author", author);
        post.put("authorImageUrl", authorImageUrl);
        post.put("imageUrl", imageDownloadUrl);
        post.put("description", description);
        post.put("postedAt", postedAt);

        dbRef.child(pid).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addPostImageView.setImageBitmap(null);
                        addPostDescription.setText("");
                        lpi.setVisibility(View.GONE);
                        addPostBtn.setEnabled(true);
                        Toast.makeText(getContext(), "Post uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lpi.setVisibility(View.GONE);
                        addPostBtn.setEnabled(true);
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}