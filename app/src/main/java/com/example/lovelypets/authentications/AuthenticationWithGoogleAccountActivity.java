package com.example.lovelypets.authentications;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthenticationWithGoogleAccountActivity extends AppCompatActivity implements UserExistenceChecker{
    ImageView cancelImageView;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    ProgressBar progressBar;
    TextView loginGotextView;
    TextView signupGoogleTextView;
    TextView signUpEmailTextView;
    CheckBox conventionCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_google);

        progressBar = findViewById(R.id.signup_bar);
        loginGotextView = findViewById(R.id.login_go);
        cancelImageView = findViewById(R.id.cancel_icon);
        conventionCheckBox = findViewById(R.id.convention_checkbox);
        signupGoogleTextView = findViewById(R.id.signup_google);
        signUpEmailTextView = findViewById(R.id.signup_with_email);
        firebaseDatabase = FirebaseDatabase.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        signupGoogleTextView.setOnClickListener(v -> {
            if (!conventionCheckBox.isChecked()) {
                conventionCheckBox.setError("!");
                return;
            }
            signUpWithGoogle();
        });

        conventionCheckBox.setOnClickListener(v -> {
            conventionCheckBox.setError(null);
            return;
        });

        signUpEmailTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
            finish();
        });

        loginGotextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        });

        cancelImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void signUpWithGoogle() {
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
                                    startActivity(new Intent(AuthenticationWithGoogleAccountActivity.this, LovelyPetsApplicationActivity.class));
                                } else {
                                    startActivity(new Intent(AuthenticationWithGoogleAccountActivity.this, InputDataForUserActivity.class));
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