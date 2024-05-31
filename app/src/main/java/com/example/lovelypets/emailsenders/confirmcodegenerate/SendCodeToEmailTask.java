package com.example.lovelypets.emailsenders.confirmcodegenerate;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lovelypets.dto.FirebaseAuthUserDTO;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class SendCodeToEmailTask extends AsyncTask<Void, Void, Void> {
    private final VerificationCodeGeneratedListener verificationCodeGeneratedListener;
    String fromEmail = "lovelypetssupteam@gmail.com";
    String emailHost = "smtp.gmail.com";
    String smtpPort = "465";
    private Integer verificationCode;
    private final FirebaseAuthUserDTO firebaseAuthUserDTO;

    public SendCodeToEmailTask(VerificationCodeGeneratedListener verificationCodeGeneratedListener, FirebaseAuthUserDTO firebaseAuthUserDTO) {
        super();
        this.verificationCodeGeneratedListener = verificationCodeGeneratedListener;
        this.firebaseAuthUserDTO = firebaseAuthUserDTO;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            sendVerificationCodeToEmail();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Handle any post-execution tasks here
    }

    private void sendVerificationCodeToEmail() {
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
            message.setSubject("Verification Code");

            verificationCode = generateCode();
            verificationCodeGeneratedListener.onVerificationCodeGenerated(verificationCode);

            message.setText("Hello, " + "\n"+
                    "\n" +
                    "Use the next verification code to confirm your email: " + verificationCode + '\n' +
                    "\n" +
                    "If you didn’t ask to verify this address, you can ignore this email.\n" +
                    "\n" +
                    "Thanks,\n" +
                    "\n" +
                    "Your LovelyPets team");
            Transport.send(message);
            Log.d(TAG, "Message send!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error. Message cannot send!");
        }
    }
    private Integer generateCode() {
        Random generateCode = new Random();
        int min = 100000;
        //return from 1 0 0 0 0 0 to 9 9 9 9 9 9
        return generateCode.nextInt(899999) + min;
    }

    public int getVerificationCode() {
        return verificationCode;
    }
}
