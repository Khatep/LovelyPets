package com.example.lovelypets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.lovelypets.enums.AuthProvider;
import com.example.lovelypets.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User implements Parcelable {
    private String email;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private Gender gender;
    private AuthProvider authProvider;
    private final List<Product> cart = new ArrayList<>();

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

    public User() {
        this.email = "email";
        this.name = "name";
        this.surname = "surname";
        this.birthDate = LocalDate.of(2021, 5, 20);
        this.phoneNumber = "phoneNumber";
        this.password = "password";
        this.gender = Gender.MALE;
        this.authProvider = AuthProvider.GOOGLE;
    }

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
        name = in.readString();
        surname = in.readString();
        phoneNumber = in.readString();
        gender = Gender.valueOf(in.readString());
        authProvider = AuthProvider.valueOf(in.readString());
        birthDate = (LocalDate) in.readSerializable();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(phoneNumber);
        parcel.writeString(gender.name());
        parcel.writeString(authProvider.name());
        parcel.writeSerializable(birthDate);
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

    public List<Product> getCart() {
        return cart;
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
        return Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(birthDate, user.birthDate) && Objects.equals(phoneNumber, user.phoneNumber) && gender == user.gender && authProvider == user.authProvider && Objects.equals(cart, user.cart);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, birthDate, phoneNumber, gender, authProvider, cart);
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
                ", products=" + cart +
                '}';
    }
}
