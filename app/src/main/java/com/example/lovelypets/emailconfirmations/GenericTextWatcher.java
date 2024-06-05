package com.example.lovelypets.emailconfirmations;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * A generic TextWatcher implementation to automatically move the focus to the next EditText
 * when a character is entered in the current EditText.
 */
public class GenericTextWatcher implements TextWatcher {
    private static final String TAG = "GenericTextWatcher";

    private final EditText currentEditText;
    private final EditText nextEditText;

    /**
     * Constructs a GenericTextWatcher.
     *
     * @param currentEditText The current EditText in which the user is typing.
     * @param nextEditText    The next EditText to move the focus to.
     */
    public GenericTextWatcher(EditText currentEditText, EditText nextEditText) {
        this.currentEditText = currentEditText;
        this.nextEditText = nextEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No action needed before text changes
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // No action needed while text is changing
    }

    @Override
    public void afterTextChanged(Editable s) {
        // If the current EditText contains a single character, move focus to the next EditText
        if (s.length() == 1 && nextEditText != null) {
            Log.d(TAG, "Character entered in " + currentEditText.getId() + ". Moving focus to " + nextEditText.getId());
            nextEditText.requestFocus();
        }
    }
}