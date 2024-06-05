package com.example.lovelypets.emailsenders.receiptgenerate;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lovelypets.dtos.FirebaseAuthUserDTO;
import com.example.lovelypets.models.Order;
import com.example.lovelypets.models.Product;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * AsyncTask to send a receipt to the user's email address.
 */
public class SendReceiptToEmailTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "SendReceiptToEmailTask";
    private static final String FROM_EMAIL = "lovelypetssupteam@gmail.com";
    private static final String EMAIL_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";

    private final ReceiptGeneratedListener receiptGeneratedListener;
    private final FirebaseAuthUserDTO firebaseAuthUserDTO;
    private final Order order;

    /**
     * Constructor for SendReceiptToEmailTask.
     *
     * @param receiptGeneratedListener Listener for receipt generation events
     * @param firebaseAuthUserDTO      User DTO containing user information
     * @param order                    The order for which the receipt is generated
     */
    public SendReceiptToEmailTask(ReceiptGeneratedListener receiptGeneratedListener, FirebaseAuthUserDTO firebaseAuthUserDTO, Order order) {
        this.receiptGeneratedListener = receiptGeneratedListener;
        this.firebaseAuthUserDTO = firebaseAuthUserDTO;
        this.order = order;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            sendReceiptToEmail(order);
        } catch (Exception e) {
            Log.e(TAG, "Error sending receipt email", e);
        }
        return null;
    }

    /**
     * Sends the receipt to the user's email address.
     *
     * @param order The order for which the receipt is generated
     */
    private void sendReceiptToEmail(Order order) {
        String userEmail = firebaseAuthUserDTO.getEmail();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", EMAIL_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", SMTP_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        // Use a secure method to retrieve the password
                        return new PasswordAuthentication(FROM_EMAIL, "mtzmrumclvxbglyn");
                    }
                }
        );

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Receipt");

            StringBuilder productsText = new StringBuilder();
            int productCounter = 1;
            for (Product p : order.getProducts()) {
                productsText.append(productCounter)
                        .append(". ")
                        .append(p.getName())
                        .append(" | ")
                        .append(p.getPrice())
                        .append(" KZT | 1\n");
                productCounter++;
            }

            message.setText(
                    "Lovely Pets Ltd.\n" +
                            "TIN: 1234567890, \n" +
                            "KPP: 0987654321\n" +
                            "Almaty city, Saina street, h. 79\n" +
                            "Phone number. +7(771) 285 86 06\n" +
                            "\n" +
                            String.format("%40s", "RECEIPT") +
                            "\n" +
                            "\n" +
                            "Order number: " + order.getOrderNumber() +
                            '\n' +
                            "Date: " + order.getDateOfCreated() +
                            "\n" +
                            "\n" +
                            "Name | Price | Amount\n" +
                            productsText +
                            "\n\n" +
                            "TOTAL: " + order.getTotalPrice() + " KZT\n" +
                            "VAT (12%): " + (int) (order.getTotalPrice() * 0.12) + " KZT\n" +
                            "\n" +
                            "PAYABLE: " + order.getTotalPrice() + " KZT\n" +
                            "Payment method: Bank card\n" +
                            "\n" +
                            "Thanks for your purchase,\n" +
                            "\n" +
                            "Your LovelyPets team"
            );

            Transport.send(message);
            Log.d(TAG, "Message sent successfully to " + userEmail);
        } catch (Exception e) {
            Log.e(TAG, "Error sending email to " + userEmail, e);
        }

        receiptGeneratedListener.onReceiptGenerated();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Handle any post-execution tasks here if necessary
        Log.d(TAG, "Receipt generation task completed.");
    }
}
