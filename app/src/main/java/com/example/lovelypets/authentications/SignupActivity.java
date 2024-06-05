package com.example.lovelypets.authentications;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.dtos.FirebaseAuthUserDTO;
import com.example.lovelypets.emailconfirmations.EmailConfirmActivity;
import com.example.lovelypets.emailsenders.confirmcodegenerate.SendCodeToEmailTask;
import com.example.lovelypets.emailsenders.confirmcodegenerate.VerificationCodeGeneratedListener;
import com.example.lovelypets.welcomepage.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
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
 * Activity for handling user signup via email/password and Google sign-in.
 */
public class SignupActivity extends AppCompatActivity implements VerificationCodeGeneratedListener, UserExistenceChecker {

    private FirebaseAuth mAuth;
    private FirebaseAuthUserDTO firebaseAuthUserDTO;
    private ConstraintLayout googleLayout;
    private GoogleSignInClient googleSignInClient;
    private CheckBox conventionCheckBox;
    private ProgressBar progressBar;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private Button signupButton;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToActivity(LovelyPetsApplicationActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeViews();
        setupGoogleSignIn();

        setUpListeners();
        setLoginTextStyle();
    }

    /**
     * Initializes views from the layout.
     */
    private void initializeViews() {
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        editTextEmail = findViewById(R.id.signup_email);
        editTextPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar);
        conventionCheckBox = findViewById(R.id.convention_checkbox);
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
     * Sets up listeners for UI elements.
     */
    private void setUpListeners() {
        editTextEmail.setOnClickListener(v -> emailLayout.setError(null));
        editTextPassword.setOnClickListener(v -> passwordLayout.setError(null));
        conventionCheckBox.setOnClickListener(v -> conventionCheckBox.setError(null));

        findViewById(R.id.login_go).setOnClickListener(v -> navigateToActivity(LoginActivity.class));
        findViewById(R.id.back_arrow).setOnClickListener(v -> navigateToActivity(WelcomeActivity.class));

        signupButton.setOnClickListener(v -> signUpUser());

        googleLayout.setOnClickListener(v -> authenticationWithGoogle());
    }

    /**
     * Sets bold style to the "LOGIN here" part of the loginGoTextView text.
     */
    private void setLoginTextStyle() {
        String fullText = getResources().getString(R.string.already_have_login_here);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("LOGIN here");
        if (startIndex != -1) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + "LOGIN here".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ((TextView) findViewById(R.id.login_go)).setText(spannableString);
    }

    /**
     * Handles user signup with email and password.
     */
    private void signUpUser() {
        String email = Objects.requireNonNull(editTextEmail.getText()).toString();
        String password = Objects.requireNonNull(editTextPassword.getText()).toString();

        if (isInputInvalid(email, password)) return;

        showProgress(true);

        checkIfUserExists(email, exists -> {
            if (exists) {
                emailLayout.setError(getString(R.string.error_user_already_exists));
                showProgress(false);
            } else {
                firebaseAuthUserDTO = new FirebaseAuthUserDTO(email, password);
                sendCodeToEmail(firebaseAuthUserDTO);
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
            emailLayout.setError(getString(R.string.error_email_empty));
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.error_password_empty));
            return true;
        }

        if (!email.contains("@") || !email.contains(".")) {
            emailLayout.setError(getString(R.string.error_incorrect_email));
            return true;
        }

        if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.error_incorrect_password_length));
            return true;
        }

        if (!conventionCheckBox.isChecked()) {
            conventionCheckBox.setError("!");
            return true;
        }

        return false;
    }

    /**
     * Shows or hides the progress bar and signup button.
     *
     * @param show true to show progress bar, false to hide it
     */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        signupButton.setVisibility(show ? View.GONE : View.VISIBLE);
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
     * Initiates the Google Sign-In process.
     */
    private void authenticationWithGoogle() {
        showProgress(true);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
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
            Toast.makeText(getApplicationContext(), "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            showProgress(false);
            Log.e("Sign Up", "Google sign up failed", e);
        }
    }

    /**
     * Authenticates the user with Firebase using the Google account.
     *
     * @param acct The GoogleSignInAccount
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                checkIfUserExists(acct.getEmail(), exists -> {
                    if (exists) {
                        navigateToActivity(LovelyPetsApplicationActivity.class);
                    } else {
                        navigateToActivity(InputDataForUserActivity.class);
                        showProgress(false);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Firebase Authentication failed", Toast.LENGTH_SHORT).show();
                showProgress(false);
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
                listener.onResult(false);
                Toast.makeText(getApplicationContext(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Sends a verification code to the user's email.
     *
     * @param firebaseAuthUserDTO The user's authentication details
     */
    public void sendCodeToEmail(FirebaseAuthUserDTO firebaseAuthUserDTO) {
        SendCodeToEmailTask sendCodeToEmailTask = new SendCodeToEmailTask(this, firebaseAuthUserDTO);
        sendCodeToEmailTask.execute();
        Toast.makeText(getApplicationContext(), "Code sent to email", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVerificationCodeGenerated(int code) {
        Intent intent = new Intent(getApplicationContext(), EmailConfirmActivity.class);
        intent.putExtra("verification_code", code);
        intent.putExtra("email", firebaseAuthUserDTO.getEmail());
        intent.putExtra("password", firebaseAuthUserDTO.getPassword());
        startActivity(intent);
        finish();
    }
}