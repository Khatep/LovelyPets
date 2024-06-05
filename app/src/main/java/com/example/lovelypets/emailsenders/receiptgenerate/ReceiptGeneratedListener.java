package com.example.lovelypets.emailsenders.receiptgenerate;

/**
 * Listener interface for receipt generation events.
 */
public interface ReceiptGeneratedListener {

    /**
     * Called when the receipt has been successfully generated and sent.
     */
    void onReceiptGenerated();
}
