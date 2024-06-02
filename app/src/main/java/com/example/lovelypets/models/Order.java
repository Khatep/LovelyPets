package com.example.lovelypets.models;

import com.example.lovelypets.enums.OrderStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private Integer orderNumber;
    private LocalDate dateOfCreated;
    private String addressOfDelivery;
    private Double totalPrice;
    private OrderStatus orderStatus;
    private final List<Product> products = new ArrayList<>();

    public Order(Integer orderNumber, LocalDate dateOfCreated, String addressOfDelivery, Double totalPrice) {
        this.orderNumber = orderNumber;
        this.dateOfCreated = dateOfCreated;
        this.addressOfDelivery = addressOfDelivery;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.PAID;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(LocalDate dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public String getAddressOfDelivery() {
        return addressOfDelivery;
    }

    public void setAddressOfDelivery(String addressOfDelivery) {
        this.addressOfDelivery = addressOfDelivery;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Product> getProducts() {
        return products;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getOrderNumber(), order.getOrderNumber()) && Objects.equals(getDateOfCreated(), order.getDateOfCreated()) && Objects.equals(getAddressOfDelivery(), order.getAddressOfDelivery()) && Objects.equals(getTotalPrice(), order.getTotalPrice()) && getOrderStatus() == order.getOrderStatus() && Objects.equals(getProducts(), order.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderNumber(), getDateOfCreated(), getAddressOfDelivery(), getTotalPrice(), getOrderStatus(), getProducts());
    }

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
