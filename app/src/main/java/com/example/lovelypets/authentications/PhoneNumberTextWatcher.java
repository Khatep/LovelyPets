package com.example.lovelypets.authentications;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

/**
 * PhoneNumberTextWatcher formats the phone number input as XXX-XXX-XXXX.
 * It ensures that only numerical characters are considered and formats
 * them according to the predefined template.
 */
public class PhoneNumberTextWatcher implements TextWatcher {
    private static final String TAG = "PhoneNumberTextWatcher";
    private static final String TEMPLATE = "XXX-XXX-XXXX"; // Template for phone number format
    private static final char NUM_PLACE = 'X'; // Placeholder character in the template

    private final StringBuilder sb = new StringBuilder();
    private boolean ignore;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // No action needed before text changes
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // No action needed during text changes
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!ignore) {
            Log.d(TAG, "Formatting phone number");
            removeFormat(editable.toString());
            applyFormat(sb.toString());
            ignore = true;
            editable.replace(0, editable.length(), sb.toString());
            ignore = false;
            Log.d(TAG, "Formatted phone number: " + sb.toString());
        }
    }

    /**
     * Removes all non-numeric characters from the input text.
     *
     * @param text The input text to be cleaned.
     */
    private void removeFormat(String text) {
        sb.setLength(0);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isNumberChar(c)) {
                sb.append(c);
            }
        }
        Log.d(TAG, "Cleaned number: " + sb.toString());
    }

    /**
     * Applies the phone number format template to the cleaned input text.
     *
     * @param text The cleaned input text containing only numeric characters.
     */
    private void applyFormat(String text) {
        sb.setLength(0);
        for (int i = 0, textIndex = 0; i < TEMPLATE.length() && textIndex < text.length(); i++) {
            if (TEMPLATE.charAt(i) == NUM_PLACE) {
                sb.append(text.charAt(textIndex));
                textIndex++;
            } else {
                sb.append(TEMPLATE.charAt(i));
            }
        }
        Log.d(TAG, "Formatted number with template: " + sb.toString());
    }

    /**
     * Checks if the character is a numeric digit.
     *
     * @param c The character to check.
     * @return True if the character is a numeric digit, false otherwise.
     */
    private boolean isNumberChar(char c) {
        return c >= '0' && c <= '9';
    }
}
