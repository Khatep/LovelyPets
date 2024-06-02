package com.example.lovelypets.authentications;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.welcomepage.WelcomeActivity;
import com.example.lovelypets.dto.FirebaseAuthUserDTO;
import com.example.lovelypets.emailconfirmations.EmailConfirmActivity;
import com.example.lovelypets.emailsenders.confirmcodegenerate.SendCodeToEmailTask;
import com.example.lovelypets.emailsenders.confirmcodegenerate.VerificationCodeGeneratedListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity implements VerificationCodeGeneratedListener, UserExistenceChecker {
    private ImageView backArrowImageView;
    private TextInputEditText editTextEmail, editTextPassword;
    private TextInputLayout emailLayout, passwordLayout;
    private Button signupButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;
    private TextView loginGotextView;
    private CheckBox conventionCheckBox;
    private FirebaseAuthUserDTO firebaseAuthUserDTO;
    ConstraintLayout googleLayout;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
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
        setContentView(R.layout.activity_signup);
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        editTextEmail = findViewById(R.id.signup_email);
        editTextPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        loginGotextView = findViewById(R.id.login_go);
        backArrowImageView = findViewById(R.id.back_arrow);
        conventionCheckBox = findViewById(R.id.convention_checkbox);

        googleLayout = findViewById(R.id.google_frame_layout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        editTextEmail.setOnClickListener(v -> {
            emailLayout.setError(null);
        });

        editTextPassword.setOnClickListener(v -> {
            passwordLayout.setError(null);
        });

        loginGotextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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

        googleLayout.setOnClickListener(v -> {
            authenticationWithGoogle();
        });

        backArrowImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });

        String fullText = getResources().getString(R.string.already_have_login_here);

        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("LOG");
        if (startIndex != -1) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + "LOGIN here".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        TextView textView = loginGotextView;
        textView.setText(spannableString);
    }

    private void authenticationWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "onActivityResult::task worked successful", Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                } catch (ApiException e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "onActivityResult::task didn't work successful", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "firebaseAuthWithGoogle::onComplete::task worked successful", Toast.LENGTH_SHORT).show();
                        //Log.d("TAG", "signInWithCredential:success");
                        checkIfUserExists(acct.getEmail(), exists -> {
                            if (exists) {
                                startActivity(new Intent(SignupActivity.this, LovelyPetsApplicationActivity.class));
                            } else {
                                startActivity(new Intent(SignupActivity.this, InputDataForUserActivity.class));
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "firebaseAuthWithGoogle::onComplete::task didn't work successful", Toast.LENGTH_SHORT).show();
                    }
                });
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
        Toast.makeText(getApplicationContext(), "Code send to email", Toast.LENGTH_SHORT).show();
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