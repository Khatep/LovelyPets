package com.example.lovelypets.dtos;

import java.util.Objects;

/**
 * A Data Transfer Object (DTO) representing a Firebase Authentication User.
 */
public class FirebaseAuthUserDTO {
    private String email;
    private String password;

    /**
     * Constructs a FirebaseAuthUserDTO with the specified email and password.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     */
    public FirebaseAuthUserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o The reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirebaseAuthUserDTO that = (FirebaseAuthUserDTO) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}