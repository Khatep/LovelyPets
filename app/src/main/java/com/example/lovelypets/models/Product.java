package com.example.lovelypets.models;

import java.util.Objects;

public class Product {
    private String iconName;
    private String name;
    private String description;
    private String categoryId;
    private Long price;

    public Product(String iconName, String name, String description, String categoryId, Long price) {
        this.iconName = iconName;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getIconName() == product.getIconName() && Objects.equals(getName(), product.getName()) && Objects.equals(getDescription(), product.getDescription()) && Objects.equals(getCategoryId(), product.getCategoryId()) && Objects.equals(getPrice(), product.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIconName(), getName(), getDescription(), getCategoryId(), getPrice());
    }
    @Override
    public String toString() {
        return "Product{" +
                "iconId=" + iconName +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", price=" + price +
                '}';
    }
}
