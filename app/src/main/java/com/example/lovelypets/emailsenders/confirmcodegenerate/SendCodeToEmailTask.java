package com.example.lovelypets.emailsenders.confirmcodegenerate;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lovelypets.dtos.FirebaseAuthUserDTO;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Asynchronous task to send a verification code to a user's email.
 */
public class SendCodeToEmailTask extends AsyncTask<Void, Void, Void> {
    private final VerificationCodeGeneratedListener verificationCodeGeneratedListener;
    private final String fromEmail = "lovelypetssupteam@gmail.com";
    private final String emailHost = "smtp.gmail.com";
    private final String smtpPort = "465";
    private Integer verificationCode;
    private final FirebaseAuthUserDTO firebaseAuthUserDTO;

    /**
     * Constructor for SendCodeToEmailTask.
     *
     * @param verificationCodeGeneratedListener Listener to handle the generated verification code.
     * @param firebaseAuthUserDTO              Data transfer object containing user email.
     */
    public SendCodeToEmailTask(VerificationCodeGeneratedListener verificationCodeGeneratedListener, FirebaseAuthUserDTO firebaseAuthUserDTO) {
        super();
        this.verificationCodeGeneratedListener = verificationCodeGeneratedListener;
        this.firebaseAuthUserDTO = firebaseAuthUserDTO;
    }

    /**
     * Performs the email sending operation in the background.
     *
     * @param voids No parameters are needed.
     * @return Always returns null.
     */
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            sendVerificationCodeToEmail();
        } catch (Exception e) {
            Log.e(TAG, "Error in sending verification code to email", e);
        }
        return null;
    }

    /**
     * Post-execution tasks, if any.
     *
     * @param aVoid No parameters are needed.
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // Handle any post-execution tasks here
    }

    /**
     * Sends a verification code to the user's email.
     */
    private void sendVerificationCodeToEmail() {
        String userEmail = firebaseAuthUserDTO.getEmail();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", emailHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.auth", "true");

        properties.put("mail.smtp.socketFactory.port", smtpPort);
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

            // Generate the verification code
            verificationCode = generateCode();
            verificationCodeGeneratedListener.onVerificationCodeGenerated(verificationCode);

            message.setText("Hello, " + "\n" +
                    "\n" +
                    "Use the next verification code to confirm your email: " + verificationCode + '\n' +
                    "\n" +
                    "If you didn’t ask to verify this address, you can ignore this email.\n" +
                    "\n" +
                    "Thanks,\n" +
                    "\n" +
                    "Your LovelyPets team");
            Transport.send(message);
            Log.d(TAG, "Message sent!");
        } catch (Exception e) {
            Log.e(TAG, "Error. Message cannot be sent!", e);
        }
    }

    /**
     * Generates a random 6-digit verification code.
     *
     * @return The generated verification code.
     */
    private Integer generateCode() {
        Random generateCode = new Random();
        int min = 100000;
        // Generate a random number between 100000 and 999999
        return generateCode.nextInt(899999) + min;
    }

    /**
     * Gets the generated verification code.
     *
     * @return The verification code.
     */
    public int getVerificationCode() {
        return verificationCode;
    }
}