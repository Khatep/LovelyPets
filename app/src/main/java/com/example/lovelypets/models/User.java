package com.example.lovelypets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.lovelypets.enums.AuthProvider;
import com.example.lovelypets.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@code User} class represents a user in the application.
 * It includes information such as email, password, name, surname, birth date, phone number,
 * gender, authentication provider, and a list of products associated with the user.
 */
public class User implements Parcelable {
    private String email;
    private String password;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phoneNumber;
    private Gender gender;
    private AuthProvider authProvider;
    private final List<Product> products = new ArrayList<>();

    /**
     * Constructs a new User with the specified details.
     *
     * @param email        the email of the user
     * @param password     the password of the user
     * @param name         the name of the user
     * @param surname      the surname of the user
     * @param birthDate    the birth date of the user
     * @param phoneNumber  the phone number of the user
     * @param gender       the gender of the user
     * @param authProvider the authentication provider of the user
     */
    public User(String email, String password, String name, String surname, LocalDate birthDate, String phoneNumber, Gender gender, AuthProvider authProvider) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.authProvider = authProvider;
    }

    /**
     * Constructs a new User with default values.
     */
    public User() {
        this.email = "email";
        this.password = "password";
        this.name = "name";
        this.surname = "surname";
        this.birthDate = LocalDate.of(2021, 5, 20);
        this.phoneNumber = "phoneNumber";
        this.gender = Gender.MALE;
        this.authProvider = AuthProvider.GOOGLE;
    }

    /**
     * Constructs a new User from a Parcel.
     *
     * @param in the Parcel containing the user data
     */
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

    /**
     * Describes the contents of the parcel.
     *
     * @return an integer bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the User object to a Parcel.
     *
     * @param parcel the Parcel to write to
     * @param i      additional flags about how the object should be written
     */
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

    // Getter and setter methods

    /**
     * Returns the email of the user.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the name of the user.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the surname of the user.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the surname of the user.
     *
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Returns the birth date of the user.
     *
     * @return the birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the user.
     *
     * @param birthDate the birth date to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Returns the phone number of the user.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the list of products associated with the user.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Returns the gender of the user.
     *
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Returns the authentication provider of the user.
     *
     * @return the authentication provider
     */
    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    /**
     * Sets the authentication provider of the user.
     *
     * @param authProvider the authentication provider to set
     */
    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(birthDate, user.birthDate) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                gender == user.gender &&
                authProvider == user.authProvider &&
                Objects.equals(products, user.products);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(email, password, name, surname, birthDate, phoneNumber, gender, authProvider, products);
    }

    /**
     * Returns a string representation of the user.
     *
     * @return a string representation of the object
     */
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
