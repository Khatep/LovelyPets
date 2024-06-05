package com.example.lovelypets.payment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

/**
 * A TextWatcher implementation to format credit card details input fields such as
 * credit card number, expiry date, CVV, and cardholder name.
 */
public class CreditCardTextWatcher implements TextWatcher {
    private static final String TAG = "CreditCardTextWatcher";
    private final String templateType;
    private final StringBuilder sb = new StringBuilder();
    private boolean ignore;

    /**
     * Constructor to initialize the watcher with the specific template type.
     *
     * @param templateType The type of the credit card field (e.g., "CREDIT_CARD_NUMBER", "CREDIT_CARD_DATE").
     */
    public CreditCardTextWatcher(String templateType) {
        this.templateType = templateType;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // No operation before text changes
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // No operation during text changes
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!ignore) {
            try {
                if (templateType.equals("CREDIT_CARD_NAME")) {
                    applyFormatForName(editable.toString());
                } else if (templateType.equals("CREDIT_CARD_NUMBER") ||
                        templateType.equals("CREDIT_CARD_DATE") ||
                        templateType.equals("CREDIT_CARD_CVV")) {
                    removeFormat(editable.toString());
                    applyFormatForNumbers(sb.toString());
                }
                ignore = true;
                editable.replace(0, editable.length(), sb.toString());
                Log.d(TAG, "Formatted text: " + sb.toString());
            } finally {
                ignore = false;
            }
        }
    }

    /**
     * Removes any non-numeric characters from the input text.
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
        Log.d(TAG, "Removed format, intermediate text: " + sb.toString());
    }

    /**
     * Applies the specific format for credit card numbers, dates, or CVVs.
     *
     * @param text The cleaned text to be formatted.
     */
    private void applyFormatForNumbers(String text) {
        sb.setLength(0);
        String template = getTemplate();
        int textIndex = 0;
        for (int i = 0; i < template.length() && textIndex < text.length(); i++) {
            char numPlace = 'X';
            if (template.charAt(i) == numPlace) {
                sb.append(text.charAt(textIndex));
                textIndex++;
            } else {
                sb.append(template.charAt(i));
            }
        }
        Log.d(TAG, "Applied number format, final text: " + sb.toString());
    }

    /**
     * Formats the cardholder name to uppercase.
     *
     * @param text The input text to be formatted.
     */
    private void applyFormatForName(String text) {
        sb.setLength(0);
        sb.append(text.toUpperCase());
        Log.d(TAG, "Formatted name: " + sb.toString());
    }

    /**
     * Checks if the character is a numeric character.
     *
     * @param c The character to check.
     * @return True if the character is a numeric character, false otherwise.
     */
    private boolean isNumberChar(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Gets the appropriate template for the given template type.
     *
     * @return The template string based on the template type.
     */
    private String getTemplate() {
        switch (templateType) {
            case "CREDIT_CARD_NUMBER":
                return "XXXX XXXX XXXX XXXX";
            case "CREDIT_CARD_DATE":
                return "XX/XX";
            case "CREDIT_CARD_CVV":
                return "XXX";
            default:
                throw new IllegalArgumentException("Invalid template type: " + templateType);
        }
    }
}