package com.example.lovelypets.emailsenders.confirmcodegenerate;

/**
 * Interface for listening to verification code generation events.
 * Implement this interface to receive the verification code after it has been generated.
 */
public interface VerificationCodeGeneratedListener {
    /**
     * Called when a verification code has been generated.
     *
     * @param code The generated verification code.
     */
    void onVerificationCodeGenerated(int code);
}