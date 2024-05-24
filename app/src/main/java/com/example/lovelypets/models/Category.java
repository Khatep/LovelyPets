package com.example.lovelypets.models;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {
    private int iconId;
    private String name;
    private String description;
    private boolean active;

    public Category(int iconId, String name, String description) {
        this.iconId = iconId;
        this.name = name;
        this.description = description;
        this.active = true;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return isActive() == category.isActive() && Objects.equals(getName(), category.getName()) && Objects.equals(getDescription(), category.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), isActive());
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }
}
