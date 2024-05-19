package com.example.lovelypets.authentications;



import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.enums.AuthProvider;
import com.example.lovelypets.enums.Gender;
import com.example.lovelypets.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class InputDataForUserActivity extends AppCompatActivity {
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    FirebaseAuth mAuth;
    private TextInputLayout nameAndSurnameLayout, birthDateLayout, genderLayout, phoneNumberLayout;
    private TextInputEditText nameAndSurnameEditText, birthDateEditText, phoneEditText;
    private ProgressBar progressBar;
    private Button submitButton;
    private AutoCompleteTextView genderAutoCompleteTextView;
    private List<String> genders;
    private ArrayAdapter<String> gendersAdapter;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_for_user);
        FirebaseApp.initializeApp(getApplicationContext());
        nameAndSurnameLayout = findViewById(R.id.name_and_surname_layout);
        birthDateLayout = findViewById(R.id.birth_date_layout);
        phoneNumberLayout = findViewById(R.id.phone_layout);
        genderLayout = findViewById(R.id.gender_layout);

        nameAndSurnameEditText = findViewById(R.id.name_and_surname_editText);
        birthDateEditText = findViewById(R.id.birth_date);
        phoneEditText = findViewById(R.id.phone);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progress_bar);
        countryCodePicker = findViewById(R.id.country_code);
        mAuth = FirebaseAuth.getInstance();
        myRef = firebaseDatabase.getReference().child("users");

        genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        gendersAdapter  = new ArrayAdapter<>(getApplicationContext(), R.layout.tv_entity, genders);

        genderAutoCompleteTextView = findViewById(R.id.gender_auto_complete);
        genderAutoCompleteTextView.setAdapter(gendersAdapter);
        genderAutoCompleteTextView.setThreshold(1);

        genderAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            if (genders.get(position).equals("Female"))
                genderLayout.setStartIconDrawable(R.drawable.ic_baseline_female);
            else if (genders.get(position).equals("Male"))
                genderLayout.setStartIconDrawable(R.drawable.ic_baseline_male);
        });

        birthDateEditText.setOnClickListener(v -> showDatePickerDialog());

        countryCodePicker.setOnClickListener(v -> {
            countryCodePicker.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_outline_selector_only_purple));
        });

        countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                countryCodePicker.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spinner_outline_selector_only_purple));
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                countryCodePicker.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_outlined_box));
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                countryCodePicker.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_outlined_box));
            }
        });

        nameAndSurnameEditText.setOnClickListener(v -> {
            nameAndSurnameLayout.setError(null);
        });

        phoneEditText.setOnClickListener(v -> {
            phoneNumberLayout.setError(null);
        });

        genderAutoCompleteTextView.setOnClickListener(v -> {
            genderLayout.setError(null);
        });

        submitButton.setOnClickListener(v -> {
            submitForm();
        });
    }

    private void submitForm() {
        String email;
        try {
            email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        } catch (NullPointerException e) {
            email = getIntent().getStringExtra("email");
        }

        String password;
        try {
            password = Objects.requireNonNull(getIntent().getStringExtra("password"));
        } catch (NullPointerException e) {
            password = "default_password";
        }

        AuthProvider authProvider;
        if (password.equals("default_password"))
            authProvider = AuthProvider.GOOGLE;
        else
            authProvider = AuthProvider.EMAIL;

        String nameAndSurname =  Objects.requireNonNull(nameAndSurnameEditText.getText()).toString().trim() + " ";
        String name = nameAndSurname.substring(0, nameAndSurname.indexOf(" "));

        String surname = " ";
        if (nameAndSurname.substring(nameAndSurname.indexOf(" ") + 1).length() > 0)
            surname = nameAndSurname.substring(nameAndSurname.indexOf(" ") + 1) ;

        String birthDate = Objects.requireNonNull(birthDateEditText.getText()).toString().trim();
        String phoneNumber = countryCodePicker.getSelectedCountryCode() + Objects.requireNonNull(phoneEditText.getText()).toString().trim();

        Gender gender;
        if (String.valueOf(genderAutoCompleteTextView.getText()).equals("Female"))
            gender = Gender.FEMALE;
        else
            gender = Gender.MALE;

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ) {
            Toast.makeText(InputDataForUserActivity.this, "Something is wrong, please reload the app", Toast.LENGTH_LONG).show();
            return;
        }

        if (nameAndSurname.equals(" ")) {
            nameAndSurnameLayout.setError(getString(R.string.error_name_and_surname_empty));
            return;
        }

        if (TextUtils.isEmpty(birthDate)) {
            birthDateLayout.setError(getString(R.string.error_birth_date_empty));
            return;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(phoneEditText.getText()).toString().trim())) {
            phoneNumberLayout.setError(getString(R.string.error_phone_number_empty));
            return;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(genderAutoCompleteTextView.getText().toString().trim()))) {
            genderLayout.setError(getString(R.string.error_gender_empty));
        }

        progressBar.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);

        int day = Integer.parseInt(birthDate.substring(0, birthDate.indexOf("/")));
        int month = Integer.parseInt(birthDate.substring(birthDate.indexOf("/") + 1, birthDate.lastIndexOf("/")));
        int year = Integer.parseInt(birthDate.substring(birthDate.lastIndexOf("/") + 1));

        if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty() && !surname.isEmpty() && !birthDate.isEmpty() && !phoneNumber.isEmpty()) {
            //submit to a server
            User user = new User(email, password, name, surname, LocalDate.of(year, month, day), phoneNumber, gender, authProvider);
            myRef.push().setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getApplicationContext(), "submitForm::push::addOnSuccessListener::onSuccess::success", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("EXCEPTIONNNNNN", e.toString());
                    Toast.makeText(getApplicationContext(), "submitForm::push::addOnSuccessListener::onFailure::fail", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
                }
            });
            goToNextActivity();
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    private void showDatePickerDialog() {
        birthDateLayout.setError(null);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth1) -> {
                    // Month is 0-based, so add 1 to display correctly
                    String selectedDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                    birthDateEditText.setText(selectedDate);
                },
                year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void showDatePickerDialog(View view) {
    }

    public void goToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
        startActivity(intent);
        finish();
    }
}