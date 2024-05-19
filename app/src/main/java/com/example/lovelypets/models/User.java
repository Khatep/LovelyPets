package com.example.lovelypets.models;

import androidx.annotation.NonNull;

import com.example.lovelypets.enums.AuthProvider;
import com.example.lovelypets.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class User {
    private String email;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private Gender gender;
    private AuthProvider authProvider;
    private final List<?> products = new ArrayList<>();

    public User(String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }

    public User(String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, Gender gender, AuthProvider authProvider) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.gender = gender;
        this.authProvider = authProvider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<?> getProducts() {
        return products;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(birthDate, user.birthDate) && Objects.equals(phoneNumber, user.phoneNumber) && gender == user.gender && authProvider == user.authProvider && Objects.equals(products, user.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, birthDate, phoneNumber, gender, authProvider, products);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", authProvider=" + authProvider +
                ", products=" + products +
                '}';
    }
}
