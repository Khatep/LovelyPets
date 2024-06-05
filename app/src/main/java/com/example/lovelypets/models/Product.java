package com.example.lovelypets.models;

import com.example.lovelypets.enums.ProductType;

import java.util.Objects;

/**
 * The {@code Product} class represents a product in the app.
 * It includes information such as the product's icon name, name, description,
 * category ID, price, and product type.
 */
public class Product {
    private String iconName;
    private String name;
    private String description;
    private String categoryId;
    private Long price;
    private ProductType productType;

    /**
     * Constructs a new Product with the specified details.
     *
     * @param iconName    the name of the icon representing the product
     * @param name        the name of the product
     * @param description a brief description of the product
     * @param categoryId  the ID of the category this product belongs to
     * @param price       the price of the product
     * @param productType the type of the product
     */
    public Product(String iconName, String name, String description, String categoryId, Long price, ProductType productType) {
        this.iconName = iconName;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.productType = productType;
    }

    /**
     * Returns the icon name of the product.
     *
     * @return the icon name
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * Sets the icon name of the product.
     *
     * @param iconName the icon name to set
     */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    /**
     * Returns the name of the product.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the product.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the product.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the category ID of the product.
     *
     * @return the category ID
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID of the product.
     *
     * @param categoryId the category ID to set
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Returns the price of the product.
     *
     * @return the price
     */
    public Long getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the price to set
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * Returns the product type.
     *
     * @return the product type
     */
    public ProductType getProductType() {
        return productType;
    }

    /**
     * Sets the product type.
     *
     * @param productType the product type to set
     */
    public void setProductType(ProductType productType) {
        this.productType = productType;
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
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getIconName(), product.getIconName()) &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getDescription(), product.getDescription()) &&
                Objects.equals(getCategoryId(), product.getCategoryId()) &&
                Objects.equals(getPrice(), product.getPrice()) &&
                getProductType() == product.getProductType();
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getIconName(), getName(), getDescription(), getCategoryId(), getPrice(), getProductType());
    }

    /**
     * Returns a string representation of the product.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Product{" +
                "iconName='" + iconName + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", price=" + price +
                ", productType=" + productType +
                '}';
    }
}
