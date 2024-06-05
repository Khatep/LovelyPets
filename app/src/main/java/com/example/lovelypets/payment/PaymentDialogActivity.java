package com.example.lovelypets.payment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.lovelypets.R;
import com.example.lovelypets.dtos.FirebaseAuthUserDTO;
import com.example.lovelypets.emailsenders.receiptgenerate.ReceiptGeneratedListener;
import com.example.lovelypets.emailsenders.receiptgenerate.SendReceiptToEmailTask;
import com.example.lovelypets.fragments.cart.CartProductListProvider;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.models.Product;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * This class represents the payment dialog where users can input their payment details
 * and proceed with the payment process.
 */
public class PaymentDialogActivity extends Dialog implements ReceiptGeneratedListener {

    // UI components
    private TextInputEditText cardNumberTextInputEditText;
    private TextInputEditText cardholderNameTextInputEditText;
    private TextInputEditText monthAndYearTextInputEditText;
    private TextInputEditText cvvTextInputEditText;
    private TextInputLayout cardNumberInputLayout;
    private TextInputLayout cardholderNameInputLayout;
    private TextInputLayout monthAndYearInputLayout;
    private TextInputLayout cvvInputLayout;
    private Button payButton;
    private final Context context;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference;
    private final CartProductListProvider cartProductListProvider;
    private final String[] userId = {"default"};

    /**
     * Constructor for PaymentDialogActivity.
     *
     * @param context the context in which the dialog is created
     * @param cartProductListProvider the provider for the cart product list
     */
    public PaymentDialogActivity(@NonNull Context context, CartProductListProvider cartProductListProvider) {
        super(context);
        this.context = context;
        this.cartProductListProvider = cartProductListProvider;
    }

    public Button getPayButton() {
        return payButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ProgressBar progressBar;
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_dialog);

        // Initialize Firebase references
        usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        // Retrieve user ID based on the current user's email
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists() && Objects.equals(snapshot.child("email").getValue(), userEmail)) {
                        userId[0] = snapshot.getKey();
                        assert userId[0] != null;
                        Log.d("User ID", userId[0]);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("User ID Retrieval", "Error retrieving user ID", error.toException());
            }
        });

        // Initialize UI components
        cardNumberInputLayout = findViewById(R.id.card_number_layout);
        cardholderNameInputLayout = findViewById(R.id.cardholder_name_layout);
        monthAndYearInputLayout = findViewById(R.id.month_and_year_layout);
        cvvInputLayout = findViewById(R.id.cvv_layout);
        progressBar = findViewById(R.id.progress_bar);

        cardNumberTextInputEditText = findViewById(R.id.card_number);
        cardholderNameTextInputEditText = findViewById(R.id.cardholder_name);
        monthAndYearTextInputEditText = findViewById(R.id.month_and_year);
        cvvTextInputEditText = findViewById(R.id.cvv);
        payButton = findViewById(R.id.pay_button);

        // Set up text change listeners for validation
        cardNumberTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_NUMBER"));
        monthAndYearTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_DATE"));
        cardholderNameTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_NAME"));
        cvvTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_CVV"));

        // Clear errors on input click
        cardNumberTextInputEditText.setOnClickListener(v -> cardNumberInputLayout.setError(null));
        cardholderNameTextInputEditText.setOnClickListener(v -> cardholderNameInputLayout.setError(null));
        monthAndYearTextInputEditText.setOnClickListener(v -> monthAndYearInputLayout.setError(null));
        cvvTextInputEditText.setOnClickListener(v -> cvvInputLayout.setError(null));

        // Handle pay button click
        payButton.setOnClickListener(v -> {
            if (areFieldsNotEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                payButton.setVisibility(View.GONE);
                createOrder(userId[0]);
                clearUserCart(userId[0]);
                cancel();
            }
        });
    }

    /**
     * Creates an order and stores it in the database.
     *
     * @param userId the ID of the user placing the order
     */
    private void createOrder(String userId) {
        DatabaseReference orderReference = usersReference.child(userId).child("orders");
        Order order = new Order(getRandomNumber(), LocalDate.now(), "null", cartProductListProvider.getTotalPrice());

        // Add products to the order
        for (Product p : cartProductListProvider.getProductList()) {
            order.getProducts().add(p);
        }

        // Save the order in the database and send a receipt
        orderReference.push().setValue(order).addOnSuccessListener(unused -> {
            sendReceiptToEmail(new FirebaseAuthUserDTO(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), "null"), order);
            Log.d("Order Creation", "Order created successfully: " + order);
        }).addOnFailureListener(e -> {
            Log.e("Order Creation", "Failed to create order", e);
        });
    }

    /**
     * Generates a random order number.
     *
     * @return a random integer representing the order number
     */
    private Integer getRandomNumber() {
        Random random = new Random();
        return random.nextInt(9999) + 1000;
    }

    /**
     * Validates if all required fields are not empty.
     *
     * @return {@code true} if all fields are not empty; {@code false} otherwise
     */
    public boolean areFieldsNotEmpty() {
        if (TextUtils.isEmpty(Objects.requireNonNull(cardNumberTextInputEditText.getText()).toString())) {
            cardNumberInputLayout.setError(context.getString(R.string.error_card_number_empty));
            return false;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(cardholderNameTextInputEditText.getText()).toString())) {
            cardholderNameInputLayout.setError(context.getString(R.string.error_card_cardholder_name_empty));
            return false;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(monthAndYearTextInputEditText.getText()).toString())) {
            monthAndYearInputLayout.setError(context.getString(R.string.error_card_date_empty));
            return false;
        }

        if (TextUtils.isEmpty(Objects.requireNonNull(cvvTextInputEditText.getText()).toString())) {
            cvvInputLayout.setError(context.getString(R.string.error_card_cvv_empty));
            return false;
        }
        return true;
    }

    /**
     * Clears the user's cart after the order is placed.
     *
     * @param userId the ID of the user
     */
    private void clearUserCart(String userId) {
        DatabaseReference cartReference = usersReference.child(userId).child("cart");
        cartReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Clear Cart", "User cart cleared successfully");
            } else {
                Log.e("Clear Cart", "Failed to clear user cart", task.getException());
            }
        });
    }

    /**
     * Sends a receipt to the user's email.
     *
     * @param firebaseAuthUserDTO the user's authentication details
     * @param order the order details
     */
    public void sendReceiptToEmail(FirebaseAuthUserDTO firebaseAuthUserDTO, Order order) {
        SendReceiptToEmailTask sendReceiptToEmailTask = new SendReceiptToEmailTask(this, firebaseAuthUserDTO, order);
        sendReceiptToEmailTask.execute();
    }

    @Override
    public void onReceiptGenerated() {
        // Handle receipt generated event
        Log.d("Receipt Generation", "Receipt generated and sent to email");
    }
}