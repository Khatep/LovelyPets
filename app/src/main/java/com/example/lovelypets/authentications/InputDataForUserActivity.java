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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lovelypets.LovelyPetsApplicationActivity;
import com.example.lovelypets.R;
import com.example.lovelypets.enums.AuthProvider;
import com.example.lovelypets.enums.Gender;
import com.example.lovelypets.models.User;
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

/**
 * This activity handles the input data form for user registration.
 * It gathers user information such as name, birth date, gender, and phone number
 * and then submits it to Firebase.
 */
public class InputDataForUserActivity extends AppCompatActivity {
    private static final String DEFAULT_PASSWORD = "default_password";
    private static final String MALE = "Male";
    private static final String FEMALE = "Female";

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextInputLayout nameAndSurnameLayout;
    private TextInputLayout birthDateLayout;
    private TextInputLayout genderLayout;
    private TextInputLayout phoneNumberLayout;
    private TextInputEditText nameAndSurnameEditText;
    private TextInputEditText birthDateEditText;
    private TextInputEditText phoneEditText;
    private ProgressBar progressBar;
    private Button submitButton;
    private AutoCompleteTextView genderAutoCompleteTextView;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data_for_user);
        FirebaseApp.initializeApp(getApplicationContext());

        initializeViews();
        setupGenderAutoCompleteTextView();
        setupBirthDateEditText();
        setupCountryCodePicker();
        setupFormFieldClickListeners();
        setupSubmitButtonClickListener();
    }

    /**
     * Initializes the views and UI elements.
     */
    private void initializeViews() {
        nameAndSurnameLayout = findViewById(R.id.name_and_surname_layout);
        birthDateLayout = findViewById(R.id.birth_date_layout);
        phoneNumberLayout = findViewById(R.id.phone_layout);
        genderLayout = findViewById(R.id.gender_layout);

        nameAndSurnameEditText = findViewById(R.id.name_and_surname_editText);
        birthDateEditText = findViewById(R.id.birth_date);
        phoneEditText = findViewById(R.id.phone);
        submitButton = findViewById(R.id.submit_button);
        progressBar = findViewById(R.id.progress_bar);
        countryCodePicker = findViewById(R.id.country_code);
        mAuth = FirebaseAuth.getInstance();
        myRef = firebaseDatabase.getReference().child("users");
    }

    /**
     * Sets up the gender AutoCompleteTextView with a list of genders.
     */
    private void setupGenderAutoCompleteTextView() {
        List<String> genders = new ArrayList<>();
        genders.add(MALE);
        genders.add(FEMALE);
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.tv_entity, genders);

        genderAutoCompleteTextView = findViewById(R.id.gender_auto_complete);
        genderAutoCompleteTextView.setAdapter(gendersAdapter);
        genderAutoCompleteTextView.setThreshold(1);

        genderAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedGender = genders.get(position);
            if (FEMALE.equals(selectedGender)) {
                genderLayout.setStartIconDrawable(R.drawable.ic_baseline_female);
            } else if (MALE.equals(selectedGender)) {
                genderLayout.setStartIconDrawable(R.drawable.ic_baseline_male);
            }
        });
    }

    /**
     * Sets up the birth date EditText to show a DatePickerDialog when clicked.
     */
    private void setupBirthDateEditText() {
        birthDateEditText.setOnClickListener(v -> showDatePickerDialog());
    }

    /**
     * Sets up the CountryCodePicker and its dialog events.
     */
    private void setupCountryCodePicker() {
        countryCodePicker.setOnClickListener(v -> setBackgroundDrawable(R.drawable.selector_outline_light_red_corner_4dp));
        countryCodePicker.setDialogEventsListener(new CountryCodePicker.DialogEventsListener() {
            @Override
            public void onCcpDialogOpen(Dialog dialog) {
                setBackgroundDrawable(R.drawable.selector_outline_light_red_corner_4dp);
            }

            @Override
            public void onCcpDialogDismiss(DialogInterface dialogInterface) {
                resetBackgroundDrawable();
            }

            @Override
            public void onCcpDialogCancel(DialogInterface dialogInterface) {
                resetBackgroundDrawable();
            }
        });
    }

    /**
     * Sets the background drawable for the CountryCodePicker.
     *
     * @param drawable The drawable resource ID to set as the background.
     */
    private void setBackgroundDrawable(int drawable) {
        countryCodePicker.setBackground(ContextCompat.getDrawable(getApplicationContext(), drawable));
    }

    /**
     * Resets the background drawable for the CountryCodePicker to the default.
     */
    private void resetBackgroundDrawable() {
        countryCodePicker.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_outlined_box));
    }

    /**
     * Sets up click listeners for the form fields to clear error messages when clicked.
     */
    private void setupFormFieldClickListeners() {
        nameAndSurnameEditText.setOnClickListener(v -> nameAndSurnameLayout.setError(null));
        phoneEditText.setOnClickListener(v -> phoneNumberLayout.setError(null));
        phoneEditText.addTextChangedListener(new PhoneNumberTextWatcher());
        genderAutoCompleteTextView.setOnClickListener(v -> genderLayout.setError(null));
    }

    /**
     * Sets up the submit button click listener to validate and submit the form.
     */
    private void setupSubmitButtonClickListener() {
        submitButton.setOnClickListener(v -> submitForm());
    }

    /**
     * Submits the form after validating the inputs and creates a new user in Firebase.
     */
    private void submitForm() {
        String email = getEmail();
        String password = getPassword();
        AuthProvider authProvider = getAuthProvider(password);

        String nameAndSurname = getNameAndSurname();
        String name = getFirstName(nameAndSurname);
        String surname = getLastName(nameAndSurname);
        String birthDate = Objects.requireNonNull(birthDateEditText.getText()).toString().trim();
        String phoneNumber = getPhoneNumber();
        Gender gender = getGender();

        if (isFormValid(email, password, nameAndSurname, birthDate, phoneNumber, gender)) {
            progressBar.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);

            LocalDate birthDateLocalDate = parseBirthDate(birthDate);
            User user = new User(email, password, name, surname, birthDateLocalDate, phoneNumber, gender, authProvider);

            // Push the new user to Firebase database
            myRef.push().setValue(user)
                    .addOnSuccessListener(unused -> {
                        Log.d("InputDataForUserActivity", "User registration successful");
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                        goToNextActivity();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("InputDataForUserActivity", "User registration failed: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                    });
        }
    }

    /**
     * Retrieves the email from the Firebase authentication or intent extras.
     *
     * @return The email address of the user.
     */
    private String getEmail() {
        String email;
        try {
            email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        } catch (NullPointerException e) {
            email = getIntent().getStringExtra("email");
        }
        return email;
    }

    /**
     * Retrieves the password from the intent extras or returns a default password.
     *
     * @return The password of the user.
     */
    private String getPassword() {
        String password;
        try {
            password = Objects.requireNonNull(getIntent().getStringExtra("password"));
        } catch (NullPointerException e) {
            password = DEFAULT_PASSWORD;
        }
        return password;
    }

    /**
     * Determines the authentication provider based on the password.
     *
     * @param password The password of the user.
     * @return The authentication provider.
     */
    private AuthProvider getAuthProvider(String password) {
        return DEFAULT_PASSWORD.equals(password) ? AuthProvider.GOOGLE : AuthProvider.EMAIL;
    }

    /**
     * Retrieves the name and surname from the EditText.
     *
     * @return The full name and surname of the user.
     */
    private String getNameAndSurname() {
        return Objects.requireNonNull(nameAndSurnameEditText.getText()).toString().trim();
    }

    /**
     * Extracts the first name from the full name.
     *
     * @param nameAndSurname The full name and surname of the user.
     * @return The first name of the user.
     */
    private String getFirstName(String nameAndSurname) {
        return nameAndSurname.substring(0, nameAndSurname.indexOf(" "));
    }

    /**
     * Extracts the last name from the full name.
     *
     * @param nameAndSurname The full name and surname of the user.
     * @return The last name of the user.
     */
    private String getLastName(String nameAndSurname) {
        String surname = nameAndSurname.substring(nameAndSurname.indexOf(" ") + 1);
        return surname.isEmpty() ? " " : surname;
    }

    /**
     * Retrieves the phone number with the country code.
     *
     * @return The full phone number of the user.
     */
    private String getPhoneNumber() {
        return countryCodePicker.getSelectedCountryCode() + Objects.requireNonNull(phoneEditText.getText()).toString().trim();
    }

    /**
     * Retrieves the gender from the AutoCompleteTextView.
     *
     * @return The gender of the user.
     */
    private Gender getGender() {
        return FEMALE.equals(genderAutoCompleteTextView.getText().toString().trim()) ? Gender.FEMALE : Gender.MALE;
    }

    /**
     * Validates the form inputs.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @param nameAndSurname The full name and surname of the user.
     * @param birthDate The birth date of the user.
     * @param phoneNumber The phone number of the user.
     * @param gender The gender of the user.
     * @return True if the form is valid, false otherwise.
     */
    private boolean isFormValid(String email, String password, String nameAndSurname, String birthDate, String phoneNumber, Gender gender) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(InputDataForUserActivity.this, "Something is wrong, please reload the app", Toast.LENGTH_LONG).show();
            return false;
        }

        if (TextUtils.isEmpty(nameAndSurname)) {
            nameAndSurnameLayout.setError(getString(R.string.error_name_and_surname_empty));
            return false;
        }

        if (TextUtils.isEmpty(birthDate)) {
            birthDateLayout.setError(getString(R.string.error_birth_date_empty));
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberLayout.setError(getString(R.string.error_phone_number_empty));
            return false;
        }

        if (TextUtils.isEmpty(genderAutoCompleteTextView.getText().toString().trim())) {
            genderLayout.setError(getString(R.string.error_gender_empty));
            return false;
        }

        return true;
    }

    /**
     * Parses the birth date from a string to a LocalDate object.
     *
     * @param birthDate The birth date string in the format "dd/MM/yyyy".
     * @return The LocalDate object representing the birth date.
     */
    private LocalDate parseBirthDate(String birthDate) {
        String[] parts = birthDate.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        return LocalDate.of(year, month, day);
    }

    /**
     * Shows the DatePickerDialog for selecting the birth date.
     */
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

    /**
     * Navigates to the next activity.
     */
    public void goToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), LovelyPetsApplicationActivity.class);
        startActivity(intent);
        finish();
    }
}