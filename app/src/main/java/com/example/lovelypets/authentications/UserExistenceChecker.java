package com.example.lovelypets.authentications;

public interface UserExistenceChecker {
    void checkIfUserExists(String email, final OnUserExistsListener listener);
}
