package com.example.lovelypets.authentications;

/**
 * Interface for a callback to be invoked when checking if a user exists.
 */
public interface OnUserExistsListener {
    /**
     * Called when the result of the user existence check is available.
     *
     * @param exists True if the user exists, false otherwise.
     */
    void onResult(boolean exists);
}
