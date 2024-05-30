package com.example.lovelypets.payment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.lovelypets.R;

public class PaymentDialogActivity extends Dialog {
    /** Handler_Interface */
    interface IHandler {
        public void handler(String val);
    }
    IHandler listner = null;
    public Context context;

    public PaymentDialogActivity(@NonNull Context context, IHandler listner) {
        super(context);
        this.context = context;
        this.listner = listner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payment_dialog);

       /* EditText edtext = findViewById(R.id.editViewDialog);
        Button btn = findViewById(R.id.buttonDialog);
        btn.setOnClickListener(v -> {
            String val = edtext.getText().toString();
            listner.handler(val);
            dismiss();
        });*/
    }
}