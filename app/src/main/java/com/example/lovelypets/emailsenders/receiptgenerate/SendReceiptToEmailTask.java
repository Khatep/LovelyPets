package com.example.lovelypets.emailsenders.receiptgenerate;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lovelypets.dto.FirebaseAuthUserDTO;
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

public class SendReceiptToEmailTask  extends AsyncTask<Void, Void, Void> {
    private final ReceiptGeneratedListener receiptGeneratedListener;
    String fromEmail = "lovelypetssupteam@gmail.com";
    String emailHost = "smtp.gmail.com";
    String smtpPort = "465";
    private final FirebaseAuthUserDTO firebaseAuthUserDTO;
    private Order order;

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
            e.printStackTrace();
        }
        return null;
    }

    private void sendReceiptToEmail(Order order) {
        String userEmail = firebaseAuthUserDTO.getEmail();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", emailHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.auth", "true");

        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, "mtzmrumclvxbglyn");
                    }
                }
        );

        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Receipt");

            int productCounter = 1;
            StringBuilder productsText = new StringBuilder();
            for (Product p : order.getProducts()) {
                productsText
                        .append(productCounter)
                        .append(".")
                        .append(p.getName())
                        .append(" | ")
                        .append(p.getPrice())
                        .append(" KZT")
                        .append(" | ")
                        .append(" 1")
                        .append("\n");

                productCounter++;
            }

            message.setText("Lovely Pets Ltd.\n" +
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
                    "\n" + "\n" +
                    "TOTAL: " + order.getTotalPrice() + " KZT" +'\n' +
                    "VAT (12%): " + (int) (order.getTotalPrice() * 0.12) + " KZT" + '\n' +
                    "\n" +
                    "PAYABLE: " + order.getTotalPrice() + " KZT" +
                    "\n" +
                    "Payment method: Bank card\n" +
                     "\n" +
                    "Thanks for your purchase,\n" +
                    "\n" +
                    "Your LovelyPets team");
            Transport.send(message);
            Log.d(TAG, "Message send!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error. Message cannot send!");
        }

        receiptGeneratedListener.onReceiptGenerated();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Handle any post-execution tasks here
    }
}
