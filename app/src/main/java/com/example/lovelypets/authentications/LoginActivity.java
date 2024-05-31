package com.example.lovelypets.authentications;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.lovelypets.passwordreset.PasswordResetActivity;
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

public class LoginActivity extends AppCompatActivity implements UserExistenceChecker {
    ImageView backImageView;
    TextInputEditText editTextEmail, editTextPassword;
    TextInputLayout emailLayout, passwordLayout;
    Button loginButton;
    ProgressBar progressBar;
    TextView signupGoTextView, forgotPasswordTextView;
    ConstraintLayout googleLayout;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        editTextEmail = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_bar);
        signupGoTextView = findViewById(R.id.singup_go);
        backImageView = findViewById(R.id.back_arrow);
        forgotPasswordTextView = findViewById(R.id.forgot_password);
        googleLayout = findViewById(R.id.google_frame_layout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signupGoTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
            finish();
        });

        editTextEmail.setOnClickListener(v -> {
            emailLayout.setError(null);
        });

        editTextPassword.setOnClickListener(v -> {
            passwordLayout.setError(null);
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email, password;
            email = Objects.requireNonNull(editTextEmail.getText()).toString();
            password = Objects.requireNonNull(editTextPassword.getText()).toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginActivity.this, "Email empty", Toast.LENGTH_LONG).show();
                emailLayout.setError(getString(R.string.error_email_empty));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Password empty", Toast.LENGTH_LONG).show();
                passwordLayout.setError(getString(R.string.error_password_empty));
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                emailLayout.setError(getString(R.string.error_incorrect_email));
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(LoginActivity.this, "Password empty", Toast.LENGTH_LONG).show();
                editTextPassword.setError("Incorrect password length");
                passwordLayout.setError(getString(R.string.error_incorrect_password_length));
                return;
            }

            loginButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                            loginButton.setVisibility(View.VISIBLE);
                        }

                    });
        });

        backImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });

        googleLayout.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            authenticationWithGoogle();
        });

        String fullText = getResources().getString(R.string.dont_have_an_account_sign_up);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("SIGN UP here");
        if (startIndex != -1) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + "SIGN UP here".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        TextView textView = signupGoTextView;
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
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "firebaseAuthWithGoogle::onComplete::task worked successful", Toast.LENGTH_SHORT).show();
                            //Log.d("TAG", "signInWithCredential:success");

                            checkIfUserExists(acct.getEmail(), exists -> {
                                if (exists) {
                                    startActivity(new Intent(LoginActivity.this, LovelyPetsApplicationActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, InputDataForUserActivity.class));
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "firebaseAuthWithGoogle::onComplete::task didn't work successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void checkIfUserExists(String email, OnUserExistsListener listener) {
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
}