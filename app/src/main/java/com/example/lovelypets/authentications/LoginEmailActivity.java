package com.example.lovelypets.authentications;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.WelcomeActivity;
import com.example.lovelypets.passwordreset.PasswordResetActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginEmailActivity extends AppCompatActivity {
    ImageView backImageView;
    TextInputEditText editTextEmail, editTextPassword;
    TextInputLayout emailLayout, passwordLayout;
    Button loginButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView signupGoTextView, forgotPasswordTextView;

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
        setContentView(R.layout.activity_login_new);

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

        signupGoTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AuthenticationWithGoogleAccountActivity.class);
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
                Toast.makeText(LoginEmailActivity.this, "Email empty", Toast.LENGTH_LONG).show();
                emailLayout.setError(getString(R.string.error_email_empty));
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginEmailActivity.this, "Password empty", Toast.LENGTH_LONG).show();
                passwordLayout.setError(getString(R.string.error_password_empty));
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                emailLayout.setError(getString(R.string.error_incorrect_email));
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(LoginEmailActivity.this, "Password empty", Toast.LENGTH_LONG).show();
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
                            //updateUI(user);
                            Toast.makeText(LoginEmailActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginEmailActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI;
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


        String fullText = getResources().getString(R.string.dont_have_an_account_sign_up);

        // Создаем SpannableString для изменения цвета только части текста
        SpannableString spannableString = new SpannableString(fullText);

        // Получаем индекс начала слова "Sign up"
        int startIndex = fullText.indexOf("Sign up");

        // Если слово "Sign up" найдено, изменяем цвет
        if (startIndex != -1) {
            // Получаем цвет, который вы хотите применить к части текста
            int color = getResources().getColor(R.color.purple_500);
            // Устанавливаем цвет для части текста с помощью ForegroundColorSpan
            spannableString.setSpan(new ForegroundColorSpan(color), startIndex, startIndex + "Sign up".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Устанавливаем SpannableString как текст для TextView
        TextView textView = signupGoTextView;
        textView.setText(spannableString);
    }
}