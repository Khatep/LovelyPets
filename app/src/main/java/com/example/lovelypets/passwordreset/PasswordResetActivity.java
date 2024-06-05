package com.example.lovelypets.passwordreset;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lovelypets.R;
import com.example.lovelypets.authentications.LoginActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * Activity for resetting the user's password.
 */
public class PasswordResetActivity extends AppCompatActivity {
    private static final String TAG = "PasswordResetActivity";
    private Button resetButton;
    private TextInputEditText editTextEmail;
    private FirebaseAuth mAuth;
    private TextInputLayout emailLayout;
    private ProgressBar progressBar;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        // Initialize views
        ImageView backArrowImageView = findViewById(R.id.back_arrow);
        resetButton = findViewById(R.id.btnReset);
        emailLayout = findViewById(R.id.email_layout);
        editTextEmail = findViewById(R.id.reset_password_email);
        progressBar = findViewById(R.id.progress_bar);

        //Clear error when user click it
        editTextEmail.setOnClickListener(v -> emailLayout.setError(null));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Set onClickListener for the back arrow to navigate to the LoginActivity
        backArrowImageView.setOnClickListener(v -> navigateToLogin());

        // Set onClickListener for the reset button to initiate password reset
        resetButton.setOnClickListener(v -> resetPassword());
    }

    /**
     * Navigates to the LoginActivity.
     */
    private void navigateToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Initiates the password reset process by sending a password reset email to the user.
     */
    private void resetPassword() {
        String email = Objects.requireNonNull(editTextEmail.getText()).toString().trim();

        if (isInputInvalid(email)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    // Password reset email sent successfully
                    Log.d(TAG, "Password reset email sent to: " + email);
                    Toast.makeText(PasswordResetActivity.this, "Password reset instructions sent.",
                            Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                })
                .addOnFailureListener(e -> {
                    // Failed to send password reset email
                    Log.e(TAG, "Failed to send password reset email", e);
                    progressBar.setVisibility(View.GONE);
                    resetButton.setVisibility(View.VISIBLE);
                    Toast.makeText(PasswordResetActivity.this, "Failed to send password reset instructions.",
                            Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Validates the input email.
     *
     * @param email The email to validate
     * @return true if the input is invalid, false otherwise
     */
    private boolean isInputInvalid(String email) {
        if (TextUtils.isEmpty(email)) {
            setError(emailLayout, R.string.error_email_empty);
            return true;
        }

        if (!email.contains("@") || !email.contains(".")) {
            setError(emailLayout, R.string.error_incorrect_email);
            return true;
        }

        return false;
    }

    /**
     * Sets an error message on a TextInputLayout.
     *
     * @param layout           The TextInputLayout to set the error on
     * @param errorMessageResId Resource ID of the error message
     */
    private void setError(TextInputLayout layout, int errorMessageResId) {
        layout.setError(getString(errorMessageResId));
    }
}
