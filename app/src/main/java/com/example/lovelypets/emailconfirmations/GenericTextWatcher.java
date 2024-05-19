package com.example.lovelypets.emailconfirmations;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class GenericTextWatcher implements TextWatcher {
    private EditText currentEditText, nextEditText;

    public GenericTextWatcher(EditText currentEditText, EditText nextEditText) {
        this.currentEditText = currentEditText;
        this.nextEditText = nextEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 1 && nextEditText != null) {
            nextEditText.requestFocus();
        }
    }
}