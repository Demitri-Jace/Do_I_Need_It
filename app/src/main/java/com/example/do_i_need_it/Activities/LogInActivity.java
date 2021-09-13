package com.example.do_i_need_it.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.do_i_need_it.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    //text boxes
    private EditText user_email_address;
    private EditText user_password;
    //buttons
    private Button log_in_button;
    private Button go_to_register;
    //progress bar
    private ProgressBar log_in_progress_bar;
    //image view
    private ImageView login_photo;

    private FirebaseAuth accountAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //text boxes
        user_email_address = findViewById(R.id.user_log_in_email_address);
        user_password = findViewById(R.id.user_log_in_password);
        //buttons
        log_in_button = findViewById(R.id.log_in_button);
        go_to_register = findViewById(R.id.go_to_register);
        //progress bar
        log_in_progress_bar = findViewById(R.id.log_in_progress_bar);

        accountAuthentication = FirebaseAuth.getInstance();

        login_photo = findViewById(R.id.user_log_in_image);

        log_in_progress_bar.setVisibility(View.INVISIBLE);

        log_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                log_in_progress_bar.setVisibility(View.VISIBLE);
                log_in_button.setVisibility(View.INVISIBLE);

                final String email_address = user_email_address.getText().toString();
                final String password = user_password.getText().toString();

                if (email_address.isEmpty() || password.isEmpty()){

                    showToastMessage("Please Make Sure That All The Required Information Is Provided");
                    log_in_button.setVisibility(View.VISIBLE);
                    log_in_progress_bar.setVisibility(View.VISIBLE);

                }
                else {

                    signIntoAccount(email_address, password);
                    log_in_button.setVisibility(View.VISIBLE);
                    log_in_progress_bar.setVisibility(View.VISIBLE);

                }

            }
        });

        go_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent go_to_register = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(go_to_register);

            }
        });

    }

    private void signIntoAccount(String email_address, String password) {

        accountAuthentication.signInWithEmailAndPassword(email_address, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    log_in_progress_bar.setVisibility(View.INVISIBLE);
                    log_in_button.setVisibility(View.VISIBLE);

                    updateUserInterface();

                }

                else {

                    showToastMessage(task.getException().getMessage());

                }

            }
        });

    }

    private void updateUserInterface() {

        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(home);
        finish();

    }

    private void showToastMessage(String s) {

        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = accountAuthentication.getCurrentUser();

        if (user !=null){

            updateUserInterface();

        }

    }
}