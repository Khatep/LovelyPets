package com.example.lovelypets.passwordreset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lovelypets.R;
import com.example.lovelypets.authentications.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class PasswordResetActivity extends AppCompatActivity {
    ImageView cancelImageView;
    Button resetButton, backButton;
    TextInputEditText editTextEmail;
    FirebaseAuth mAuth;
    TextInputLayout emailLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        cancelImageView = findViewById(R.id.cancel_icon);
        resetButton = findViewById(R.id.btnReset);
        //backButton = findViewById(R.id.btnForgotPasswordBack);
        emailLayout = findViewById(R.id.email_layout);
        editTextEmail = findViewById(R.id.reset_password_email);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);

        cancelImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        resetButton.setOnClickListener(v -> {
            resetPassword();
        });
    }

    public void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(Objects.requireNonNull(editTextEmail.getText()).toString().trim())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PasswordResetActivity.this, "Password reset instructions send.",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        resetButton.setVisibility(View.VISIBLE);
                    }
                });
    }
}