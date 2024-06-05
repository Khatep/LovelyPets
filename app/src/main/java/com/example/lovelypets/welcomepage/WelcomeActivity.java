package com.example.lovelypets.welcomepage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.authentications.LoginActivity;
import com.example.lovelypets.authentications.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * WelcomeActivity is the entry point of the application that displays the welcome screen.
 * It checks if the user is already logged in and navigates accordingly.
 * It also provides options for the user to navigate to the login or signup screens.
 */
public class WelcomeActivity extends AppCompatActivity {
    private FirebaseUser user;
    private TextView loginTextView;
    private Button signupButton;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initializeFirebaseAuth();
        initializeUI();

        if (isUserLoggedIn()) {
            navigateToMainActivity();
        }

        setupListeners();
    }

    /**
     * Initializes the Firebase authentication instance and retrieves the current user.
     */
    private void initializeFirebaseAuth() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    /**
     * Initializes the user interface elements by finding them by their ID.
     */
    private void initializeUI() {
        loginTextView = findViewById(R.id.welcome_login_go_button);
        signupButton = findViewById(R.id.welcome_signup_go_button);
    }

    /**
     * Checks if the user is currently logged in.
     * @return true if the user is logged in, false otherwise.
     */
    private boolean isUserLoggedIn() {
        return user != null;
    }

    /**
     * Navigates to the main application activity (LovelyPetsApplicationActivity) if the user is logged in.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, LovelyPetsApplicationActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Sets up the click listeners for the login and signup buttons.
     */
    private void setupListeners() {
        loginTextView.setOnClickListener(v -> navigateToLoginActivity());
        signupButton.setOnClickListener(v -> navigateToSignupActivity());
    }

    /**
     * Navigates to the LoginActivity when the login button is clicked.
     */
    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigates to the SignupActivity when the signup button is clicked.
     */
    private void navigateToSignupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}