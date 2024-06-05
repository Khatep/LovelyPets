package com.example.lovelypets.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code Category} class represents a category in the application.
 * It includes details such as the icon ID, name, description, and whether the category is active.
 */
public class Category implements Serializable {
    private int iconId;
    private String name;
    private String description;
    private boolean active;

    /**
     * Constructs a new Category with the specified details.
     *
     * @param iconId      the icon ID of the category
     * @param name        the name of the category
     * @param description the description of the category
     */
    public Category(int iconId, String name, String description) {
        this.iconId = iconId;
        this.name = name;
        this.description = description;
        this.active = true; // Default to active
    }

    /**
     * Returns the icon ID of the category.
     *
     * @return the icon ID
     */
    public int getIconId() {
        return iconId;
    }

    /**
     * Sets the icon ID of the category.
     *
     * @param iconId the icon ID to set
     */
    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    /**
     * Returns the name of the category.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the category.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the category.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns whether the category is active.
     *
     * @return {@code true} if the category is active; {@code false} otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets whether the category is active.
     *
     * @param active {@code true} to set the category as active; {@code false} otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
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
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return isActive() == category.isActive() &&
                Objects.equals(getName(), category.getName()) &&
                Objects.equals(getDescription(), category.getDescription());
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), isActive());
    }

    /**
     * Returns a string representation of the category.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Category{" +
                "iconId=" + iconId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
