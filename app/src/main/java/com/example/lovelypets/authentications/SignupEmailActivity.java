package com.example.lovelypets.authentications;

import android.content.Intent;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.WelcomeActivity;
import com.example.lovelypets.dto.FirebaseAuthUserDTO;
import com.example.lovelypets.emailconfirmations.EmailConfirmActivity;
import com.example.lovelypets.verifications.SendCodeToEmailTask;
import com.example.lovelypets.verifications.VerificationCodeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class SignupEmailActivity extends AppCompatActivity implements VerificationCodeListener, UserExistenceChecker {
    private ImageView cancelImageView;
    private TextInputEditText editTextEmail, editTextPassword;
    private TextInputLayout emailLayout, passwordLayout;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;
    private TextView loginGotextView;
    private CheckBox conventionCheckBox;
    private FirebaseAuthUserDTO firebaseAuthUserDTO;
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        editTextEmail = findViewById(R.id.signup_email);
        editTextPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.signup_bar);
        loginGotextView = findViewById(R.id.login_go);
        cancelImageView = findViewById(R.id.cancel_icon);
        conventionCheckBox = findViewById(R.id.convention_checkbox);

        editTextEmail.setOnClickListener(v -> {
            emailLayout.setError(null);
        });

        editTextPassword.setOnClickListener(v -> {
            passwordLayout.setError(null);
        });

        loginGotextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginWithGoogleActivity.class);
            startActivity(intent);
            finish();
        });

        conventionCheckBox.setOnClickListener(v -> {
            conventionCheckBox.setError(null);
        });

        signupButton.setOnClickListener(v -> {
            String email, password;;
            email = Objects.requireNonNull(editTextEmail.getText()).toString();
            password = Objects.requireNonNull(editTextPassword.getText()).toString();

            if (TextUtils.isEmpty(email)) {
                emailLayout.setError(getString(R.string.error_email_empty));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordLayout.setError(getString(R.string.error_password_empty));
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                emailLayout.setError(getString(R.string.error_incorrect_email));
                return;
            }

            if (password.length() < 6) {
                editTextPassword.setError("Incorrect password length");
                passwordLayout.setError(getString(R.string.error_incorrect_password_length));
                return;
            }

            if (!conventionCheckBox.isChecked()) {
                conventionCheckBox.setError("!");
                return;
            }

            signupButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            checkIfUserExists(email, exists -> {
                if (exists) {
                    emailLayout.setError(getString(R.string.error_user_already_exists));
                } else {
                    firebaseAuthUserDTO = new FirebaseAuthUserDTO(email, password);
                    sendCodeToEmail(firebaseAuthUserDTO);
                }
                signupButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            });
        });

        cancelImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });

        String fullText = getResources().getString(R.string.do_you_have_an_account_login);

        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Login");
        if (startIndex != -1) {
            int color = getResources().getColor(R.color.purple_500);
            spannableString.setSpan(new ForegroundColorSpan(color), startIndex, startIndex + "Login".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        TextView textView = loginGotextView;
        textView.setText(spannableString);
    }

    @Override
    public void checkIfUserExists(String email, final OnUserExistsListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onResult(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                listener.onResult(false);
            }
        });
    }

    public void sendCodeToEmail(FirebaseAuthUserDTO firebaseAuthUserDTO) {
        SendCodeToEmailTask sendCodeToEmailTask = new SendCodeToEmailTask(this, firebaseAuthUserDTO);
        sendCodeToEmailTask.execute();
    }

    @Override
    public void onVerificationCodeGenerated(int code) {
        Intent intent = new Intent(getApplicationContext(), EmailConfirmActivity.class);
        intent.putExtra("verification_code", code);
        intent.putExtra("email", firebaseAuthUserDTO.getEmail());
        intent.putExtra("password",firebaseAuthUserDTO.getPassword());
        startActivity(intent);
        finish();
    }
}