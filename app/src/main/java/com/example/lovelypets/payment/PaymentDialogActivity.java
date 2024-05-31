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
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lovelypets.R;
import com.example.lovelypets.dto.FirebaseAuthUserDTO;
import com.example.lovelypets.emailsenders.confirmcodegenerate.SendCodeToEmailTask;
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

public class PaymentDialogActivity extends Dialog implements ReceiptGeneratedListener {
    private TextInputEditText cardNumberTextInputEditText, cardholderNameTextInputEditText, monthAndYearTextInputEditText, cvvTextInputEditText;
    private TextInputLayout cardNumberInputLayout, cardholderNameInputLayout, monthAndYearInputLayout, cvvInputLayout;
    private List<Product> orderProductList;
    private Button payButton;
    private String[] userId = {"default"};
    private final Context context;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference usersReference, orderReference, cartReference;
    private ProgressBar progressBar;
    private CartProductListProvider cartProductListProvider;
    private ReceiptGeneratedListener receiptGeneratedListener;

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
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_dialog);

        usersReference = firebaseDatabase.getReference().child("users");
        String userEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists() && Objects.equals(snapshot.child("email").getValue(), userEmail)) {
                        userId[0] = snapshot.getKey();
                        assert userId[0] != null;
                        Log.d("User ID", userId[0]);
                        break;
                        //cartProductList = getCartProductList();
                        //setCartRecycleView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        cardNumberTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_NUMBER"));
        monthAndYearTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_DATE"));
        cardholderNameTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_NAME"));
        cvvTextInputEditText.addTextChangedListener(new CreditCardTextWatcher("CREDIT_CARD_CVV"));

        cardNumberTextInputEditText.setOnClickListener(v -> {
            cardNumberInputLayout.setError(null);
        });

        cardholderNameTextInputEditText.setOnClickListener(v -> {
            cardholderNameInputLayout.setError(null);
        });

        monthAndYearTextInputEditText.setOnClickListener(v -> {
            monthAndYearInputLayout.setError(null);
        });

        cvvTextInputEditText.setOnClickListener(v -> {
            cvvInputLayout.setError(null);
        });

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
    private void createOrder(String userId) {
        orderReference = usersReference.child(userId).child("order");
        Order order = new Order(getRandomNumber(), LocalDate.now(), "almaty, 1 mkr", cartProductListProvider.getTotalPrice());

        for (Product p : cartProductListProvider.getProductList()) {
            order.getProducts().add(p);
        }

        orderReference.push().setValue(order).addOnSuccessListener(unused -> {
            sendReceiptToEmail(new FirebaseAuthUserDTO(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(), "not_important"), order);
        }).addOnFailureListener(e -> {

        });
    }

    private Integer getRandomNumber() {
        Random random = new Random();
        return random.nextInt(9999) + 1000;
    }

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

    private void clearUserCart(String userId) {
        cartReference = usersReference.child(userId).child("cart");
        cartReference.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Clear Cart", "User cart cleared successfully");
            } else {
                Log.e("Clear Cart", "Failed to clear user cart", task.getException());
            }
        });
    }

    public void sendReceiptToEmail(FirebaseAuthUserDTO firebaseAuthUserDTO, Order order) {
        SendReceiptToEmailTask sendReceiptToEmailTask = new SendReceiptToEmailTask(this, firebaseAuthUserDTO, order);
        sendReceiptToEmailTask.execute();}
    @Override
    public void onReceiptGenerated() {

    }
}