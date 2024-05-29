package com.example.lovelypets.payment;

import android.text.Editable;
import android.text.TextWatcher;

public class CreditCardTextWatcher implements TextWatcher {
    private final String templateType;
    StringBuilder sb = new StringBuilder();
    boolean ignore;
    private final char numPlace = 'X';

    public CreditCardTextWatcher(String templateType) {
        this.templateType = templateType;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @Override
    public void afterTextChanged(Editable editable) {
        if (!ignore) {
            if (templateType.equals("CREDIT_CARD_NAME")) {
                applyFormatForName(editable.toString());
            } else if (templateType.equals("CREDIT_CARD_NUMBER") || templateType.equals("CREDIT_CARD_DATE") || templateType.equals("CREDIT_CARD_CVV")) {
                removeFormat(editable.toString());
                applyFormatForNumbers(sb.toString());
            }
            ignore = true;
            editable.replace(0, editable.length(), sb.toString());
            ignore = false;
        }
    }

    private void removeFormat(String text) {
        sb.setLength(0);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isNumberChar(c)) {
                sb.append(c);
            }
        }
    }
    private void applyFormatForNumbers(String text) {
        sb.setLength(0);
        String template = getTemplate();
        for (int i = 0, textIndex = 0; i < template.length() && textIndex < text.length(); i++) {
            if (template.charAt(i) == numPlace) {
                sb.append(text.charAt(textIndex));
                textIndex++;
            } else {
                sb.append(template.charAt(i));
            }
        }
    }

    private void applyFormatForName(String text) {
        sb.setLength(0);
        sb.append(text.toUpperCase());
    }

    private boolean isNumberChar(char c) {
        return c >= '0' && c <= '9';
    }

    private String getTemplate() {
        if(templateType.equals("CREDIT_CARD_NUMBER")) {
            return "XXXX XXXX XXXX XXXX";
        } else if (templateType.equals("CREDIT_CARD_DATE")) {
            return "XX/XX";
        } else {
            return "XXX";
        }
    }
}