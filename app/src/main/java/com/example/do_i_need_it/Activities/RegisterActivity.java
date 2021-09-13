package com.example.do_i_need_it.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.do_i_need_it.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    //profile picture
    ImageView UserRegisterPhoto;
    static int PermissionRequestCode = 1;
    static int REQUEST_CODE = 1;
    Uri selected_image;

    //authentication
    //text boxes
    private EditText user_name;
    private EditText user_surname;
    private EditText user_email_address;
    private EditText user_password;
    private EditText user_confirm_password;
    //progress bar
    private ProgressBar loading_information;
    //buttons
    private Button register_button;
    private Button log_in;
    private Button go_to_log_in;

    private FirebaseAuth firebaseAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //register activity text fields
        user_name = findViewById(R.id.user_register_name);
        user_surname = findViewById(R.id.user_register_surname);
        user_email_address = findViewById(R.id.user_register_email_address);
        user_password = findViewById(R.id.user_register_password);
        user_confirm_password = findViewById(R.id.user_register_confirm_password);

        //firebase authentication
        firebaseAuthentication = FirebaseAuth.getInstance();

        //progress bar
        loading_information = findViewById(R.id.progressBar);

        loading_information.setVisibility(View.INVISIBLE);

        //button
        register_button = findViewById(R.id.register_button);
        go_to_log_in = findViewById(R.id.go_to_log_in);

        //register profile picture
        UserRegisterPhoto = findViewById(R.id.user_register_image);

        //onclick listener for the register button
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register_button.setVisibility(View.INVISIBLE);
                loading_information.setVisibility(View.VISIBLE);

                final String name = user_name.getText().toString();
                final String surname = user_surname.getText().toString();
                final String email_address = user_email_address.getText().toString();
                final String password = user_password.getText().toString();
                final String confirm_password = user_confirm_password.getText().toString();

                if (name.isEmpty() || surname.isEmpty() || email_address.isEmpty() || password.isEmpty() || !confirm_password.equals(password)) {

                    //display a message to tell the user what is wrong
                    showToastMessage("Please Make Sure That All The Required Information Is Provided");
                    register_button.setVisibility(View.VISIBLE);
                    loading_information.setVisibility(View.INVISIBLE);

                } else {

                    //if all fields are all accounted for now we register the users account
                    createUserProfileAccount(name, surname, email_address, password);

                }

            }
        });

        //profile picture
        UserRegisterPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 23) {

                    firstCheckAndRequestForPermission();

                } else {

                    openUsersGallery();

                }

            }
        });

        go_to_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_to_log_in = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(go_to_log_in);

            }
        });

    }

    //creating users account
    private void createUserProfileAccount(String name, String surname, String email_address, String password) {

        firebaseAuthentication.createUserWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            showToastMessage("Account Successfully Created");

                            updateTheUsersInformation(name, selected_image, firebaseAuthentication.getCurrentUser());

                        } else {

                            showToastMessage("Account Creation Has Failed" + task.getException().getMessage());
                            register_button.setVisibility(View.VISIBLE);
                            loading_information.setVisibility(View.INVISIBLE);

                        }

                    }
                });

    }

    //update our users information (profile picture and name)
    private void updateTheUsersInformation(String name, Uri selected_image, FirebaseUser currentUser) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user profile picture");
        StorageReference imageStoragePath = storageReference.child(selected_image.getLastPathSegment());
        imageStoragePath.putFile(selected_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageStoragePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            showToastMessage("Registration Successfully Complete");

                                            updateUserInterface();

                                        }

                                    }
                                });

                    }
                });

            }
        });

    }

    private void updateUserInterface() {

        Intent home = new Intent(RegisterActivity.this, LogInActivity.class);
        startActivity(home);
        finish();

    }

    private void showToastMessage(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

    }

    private void firstCheckAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this, "Please Accept The Required Permission", Toast.LENGTH_SHORT).show();

            } else {

                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PermissionRequestCode);

            }

        } else
            openUsersGallery();

    }

    private void openUsersGallery() {

        //TODO; has to open users gallery and allow user to pick and image!

        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            //user successfully picked image
            //now we save image reference to a uri variable

            selected_image = data.getData();
            UserRegisterPhoto.setImageURI(selected_image);


        }

    }
}