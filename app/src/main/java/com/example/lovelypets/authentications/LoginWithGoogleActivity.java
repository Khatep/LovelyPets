package com.example.lovelypets.authentications;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lovelypets.R;
import com.example.lovelypets.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



//TODO DELETE THIS CLASS
public class LoginWithGoogleActivity extends AppCompatActivity {
    private ImageView cancelImageView;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private ProgressBar progressBar;
    private TextView signupGoTextView;
    private TextView loginGoogleTextView;
    private TextView loginEmailTextView;
    private ImageView googleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_bar);
        signupGoTextView = findViewById(R.id.singup_go);
        cancelImageView = findViewById(R.id.cancel_icon);
        loginGoogleTextView = findViewById(R.id.login_google);
        loginEmailTextView = findViewById(R.id.login_with_email);
        googleImageView = findViewById(R.id.google_image);
        //firebaseDatabase = FirebaseDatabase.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        loginEmailTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginEmailActivity.class);
            startActivity(intent);
            finish();
        });

        signupGoTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AuthenticationWithGoogleAccountActivity.class);
            startActivity(intent);
            finish();
        });

        cancelImageView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        });

        googleImageView.setOnClickListener(v -> {

        });

        String fullText = getResources().getString(R.string.dont_have_an_account_sign_up);
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("Sign up");
        if (startIndex != -1) {
            int color = getResources().getColor(R.color.purple_500);
            spannableString.setSpan(new ForegroundColorSpan(color), startIndex, startIndex + "Sign up".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        TextView textView = signupGoTextView;
        textView.setText(spannableString);
    }





}