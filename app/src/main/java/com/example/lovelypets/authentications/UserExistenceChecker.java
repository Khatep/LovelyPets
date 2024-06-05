package com.example.lovelypets.authentications;

/**
 * Interface for checking if a user exists in the system.
 */
public interface UserExistenceChecker {
    /**
     * Checks if a user with the specified email exists.
     *
     * @param email    The email of the user to check.
     * @param listener The listener to notify with the result of the check.
     */
    void checkIfUserExists(String email, final OnUserExistsListener listener);
}
