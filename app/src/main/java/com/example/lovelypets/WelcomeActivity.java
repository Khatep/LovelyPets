package com.example.lovelypets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lovelypets.authentications.LoginActivity;

import com.example.lovelypets.authentications.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView loginTextView;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_new);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
            startActivity(intent);
            finish();
        }

        loginTextView = findViewById(R.id.welcome_login_go_button);
        signupButton = findViewById(R.id.welcome_signup_go_button);

        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
            finish();
        });
    }
}