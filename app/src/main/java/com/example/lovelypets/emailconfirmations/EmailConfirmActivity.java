package com.example.lovelypets.emailconfirmations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lovelypets.R;
import com.example.lovelypets.authentications.InputDataForUserActivity;
import com.example.lovelypets.authentications.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for confirming email with a verification code.
 */
public class EmailConfirmActivity extends AppCompatActivity {
    private static final String TAG = "EmailConfirmActivity";

    private ImageView backArrowImageView;
    private EditText digit1, digit2, digit3, digit4, digit5, digit6;
    private Button submitButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirm);

        initViews();

        mAuth = FirebaseAuth.getInstance();

        // Add text change listeners to move focus to the next EditText
        digit1.addTextChangedListener(new GenericTextWatcher(digit1, digit2));
        digit2.addTextChangedListener(new GenericTextWatcher(digit2, digit3));
        digit3.addTextChangedListener(new GenericTextWatcher(digit3, digit4));
        digit4.addTextChangedListener(new GenericTextWatcher(digit4, digit5));
        digit5.addTextChangedListener(new GenericTextWatcher(digit5, digit6));
        digit6.addTextChangedListener(new GenericTextWatcher(digit6, null));

        // Set up the back arrow click listener
        backArrowImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the submit button click listener
        submitButton.setOnClickListener(v -> {
            submitButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // Retrieve the confirmation code entered by the user
            String enteredCode = digit1.getText().toString() +
                    digit2.getText().toString() +
                    digit3.getText().toString() +
                    digit4.getText().toString() +
                    digit5.getText().toString() +
                    digit6.getText().toString();

            // Retrieve the sent code from the Intent extras
            int sentCode = getIntent().getIntExtra("verification_code", 0);
            if (enteredCode.equals(String.valueOf(sentCode))) {
                // Codes match, proceed with creating the user account
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");

                if (email != null && password != null) {
                    createUserWithEmailAndPassword(email, password, enteredCode);
                } else {
                    Log.e(TAG, "Email or password is null");
                    Toast.makeText(EmailConfirmActivity.this, "Email or password is missing.", Toast.LENGTH_SHORT).show();
                    submitButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                // Verification code does not match, display an error message
                Toast.makeText(EmailConfirmActivity.this, "Incorrect verification code.", Toast.LENGTH_SHORT).show();
                submitButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

        submitButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Creates a user with email and password using Firebase Authentication.
     *
     * @param email       The user's email.
     * @param password    The user's password.
     * @param enteredCode The entered verification code.
     */
    public void createUserWithEmailAndPassword(String email, String password, String enteredCode) {
        mAuth.createUserWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, navigate to InputDataForUserActivity
                        Intent intent = new Intent(getApplicationContext(), InputDataForUserActivity.class);
                        intent.putExtra("confirmation_code", enteredCode);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(TAG, "Authentication failed: " + task.getException());
                        Toast.makeText(EmailConfirmActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        submitButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(e -> Log.e(TAG, "Error creating user: " + e.getMessage()));
    }

    /**
     * Initializes views from the layout.
     */
    private void initViews() {
        backArrowImageView = findViewById(R.id.back_arrow);
        digit1 = findViewById(R.id.digit1);
        digit2 = findViewById(R.id.digit2);
        digit3 = findViewById(R.id.digit3);
        digit4 = findViewById(R.id.digit4);
        digit5 = findViewById(R.id.digit5);
        digit6 = findViewById(R.id.digit6);
        progressBar = findViewById(R.id.progress_bar);
        submitButton = findViewById(R.id.submit_button);
    }
}