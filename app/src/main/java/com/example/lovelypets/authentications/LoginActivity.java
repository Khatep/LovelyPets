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

/**
 * Activity for handling user login via email/password and Google sign-in.
 */
public class LoginActivity extends AppCompatActivity implements UserExistenceChecker {
    private static final String TAG = "LoginActivity";
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1000;

    private ImageView backImageView;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signupGoTextView;
    private TextView forgotPasswordTextView;
    private ConstraintLayout googleLayout;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToActivity(LovelyPetsApplicationActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupGoogleSignIn();

        signupGoTextView.setOnClickListener(v -> navigateToActivity(SignupActivity.class));
        editTextEmail.setOnClickListener(v -> emailLayout.setError(null));
        editTextPassword.setOnClickListener(v -> passwordLayout.setError(null));
        forgotPasswordTextView.setOnClickListener(v -> navigateToActivity(PasswordResetActivity.class));
        loginButton.setOnClickListener(v -> loginUser());
        backImageView.setOnClickListener(v -> navigateToActivity(WelcomeActivity.class));
        googleLayout.setOnClickListener(v -> startGoogleSignIn());

        setSignUpTextStyle();
    }

    /**
     * Initializes views from the layout.
     */
    private void initViews() {
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
    }

    /**
     * Sets up Google Sign-In options and initializes GoogleSignInClient.
     */
    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Handles user login with email and password.
     */
    private void loginUser() {
        String email = Objects.requireNonNull(editTextEmail.getText()).toString();
        String password = Objects.requireNonNull(editTextPassword.getText()).toString();

        if (isInputInvalid(email, password)) return;

        showProgress(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast(getString(R.string.auth_success));
                        navigateToActivity(LovelyPetsApplicationActivity.class);
                    } else {
                        showToast(getString(R.string.auth_failed));
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        showProgress(false);
                    }
                });
    }

    /**
     * Validates the email and password input fields.
     *
     * @param email    User's email
     * @param password User's password
     * @return true if input is invalid, false otherwise
     */
    private boolean isInputInvalid(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            setError(emailLayout, R.string.error_email_empty);
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            setError(passwordLayout, R.string.error_password_empty);
            return true;
        }

        if (!email.contains("@") || !email.contains(".")) {
            setError(emailLayout, R.string.error_incorrect_email);
            return true;
        }

        if (password.length() < 6) {
            setError(passwordLayout, R.string.error_incorrect_password_length);
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

    /**
     * Shows or hides the progress bar and login button.
     *
     * @param show true to show progress bar, false to hide it
     */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Navigates to the specified activity and finishes the current one.
     *
     * @param activityClass The class of the activity to navigate to
     */
    private void navigateToActivity(Class<?> activityClass) {
        startActivity(new Intent(getApplicationContext(), activityClass));
        finish();
    }

    /**
     * Sets bold style to the "SIGN UP here" part of the signupGoTextView text.
     */
    private void setSignUpTextStyle() {
        String fullText = getResources().getString(R.string.dont_have_an_account_sign_up);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("SIGN UP here");
        if (startIndex != -1) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + "SIGN UP here".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        signupGoTextView.setText(spannableString);
    }

    /**
     * Initiates the Google Sign-In process.
     */
    private void startGoogleSignIn() {
        showProgress(true);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
        }
    }

    /**
     * Handles the result of the Google Sign-In process.
     *
     * @param task The task containing the GoogleSignInAccount
     */
    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            showToast(getString(R.string.google_sign_in_failed));
            showProgress(false);
            Log.e(TAG, "Google sign in failed", e);
        }
    }

    /**
     * Authenticates the user with Firebase using Google credentials.
     *
     * @param acct The GoogleSignInAccount obtained from Google Sign-In
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkIfUserExists(acct.getEmail(), exists -> {
                            if (exists) {
                                navigateToActivity(LovelyPetsApplicationActivity.class);
                            } else {
                                navigateToActivity(InputDataForUserActivity.class);
                            }
                        });
                    } else {
                        showToast(getString(R.string.auth_failed));
                        showProgress(false);
                        Log.e(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
    }

    /**
     * Checks if a user with the given email exists in the Firebase Realtime Database.
     *
     * @param email    The email of the user to check
     * @param listener Listener to handle the result of the check
     */
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
                listener.onResult(false);
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }

    /**
     * Displays a toast message.
     *
     * @param message The message to display
     */
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}