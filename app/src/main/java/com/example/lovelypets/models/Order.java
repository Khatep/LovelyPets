package com.example.lovelypets.models;

import com.example.lovelypets.enums.OrderStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The {@code Order} class represents an order in the application.
 * It includes details such as the order number, creation date, delivery address, total price,
 * order status, and a list of products associated with the order.
 */
public class Order {
    private Integer orderNumber;
    private LocalDate dateOfCreated;
    private String addressOfDelivery;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private final List<Product> products = new ArrayList<>();

    /**
     * Constructs a new Order with the specified details.
     *
     * @param orderNumber      the order number
     * @param dateOfCreated    the date the order was created
     * @param addressOfDelivery the address for delivery
     * @param totalPrice       the total price of the order
     */
    public Order(Integer orderNumber, LocalDate dateOfCreated, String addressOfDelivery, Double totalPrice) {
        this.orderNumber = orderNumber;
        this.dateOfCreated = dateOfCreated;
        this.addressOfDelivery = addressOfDelivery;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.PAID;  // Default status is set to PAID
    }

    // Getter and setter methods

    /**
     * Returns the order number.
     *
     * @return the order number
     */
    public Integer getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order number.
     *
     * @param orderNumber the order number to set
     */
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Returns the date the order was created.
     *
     * @return the creation date
     */
    public LocalDate getDateOfCreated() {
        return dateOfCreated;
    }

    /**
     * Sets the date the order was created.
     *
     * @param dateOfCreated the creation date to set
     */
    public void setDateOfCreated(LocalDate dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    /**
     * Returns the delivery address.
     *
     * @return the delivery address
     */
    public String getAddressOfDelivery() {
        return addressOfDelivery;
    }

    /**
     * Sets the delivery address.
     *
     * @param addressOfDelivery the delivery address to set
     */
    public void setAddressOfDelivery(String addressOfDelivery) {
        this.addressOfDelivery = addressOfDelivery;
    }

    /**
     * Returns the total price of the order.
     *
     * @return the total price
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalPrice the total price to set
     */
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Returns the status of the order.
     *
     * @return the order status
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the status of the order.
     *
     * @param orderStatus the order status to set
     */
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Returns the list of products in the order.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    // Overridden methods for equality, hash code, and string representation

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return {@code true} if this object is the same as the obj argument; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderNumber(), order.getOrderNumber()) &&
                Objects.equals(getDateOfCreated(), order.getDateOfCreated()) &&
                Objects.equals(getAddressOfDelivery(), order.getAddressOfDelivery()) &&
                Objects.equals(getTotalPrice(), order.getTotalPrice()) &&
                getOrderStatus() == order.getOrderStatus() &&
                Objects.equals(getProducts(), order.getProducts());
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getOrderNumber(), getDateOfCreated(), getAddressOfDelivery(), getTotalPrice(), getOrderStatus(), getProducts());
    }

    /**
     * Returns a string representation of the order.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", dateOfCreated=" + dateOfCreated +
                ", addressOfDelivery='" + addressOfDelivery + '\'' +
                ", totalPrice=" + totalPrice +
                ", orderStatus=" + orderStatus +
                ", products=" + products +
                '}';
    }
}
