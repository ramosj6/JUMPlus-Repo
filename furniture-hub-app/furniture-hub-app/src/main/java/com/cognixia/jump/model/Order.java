package com.cognixia.jump.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn( name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn( name = "cart_id", referencedColumnName = "id")
    private ShoppingCart shoppingCart;

    @NotNull
    @Column(columnDefinition = "double default 0.0")
    private Double totalPrice;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeStamp;

    private String trackingNumber;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItemList;

    public Order(){

    }

    public Order(Integer id, User user, ShoppingCart shoppingCart, Double totalPrice, Date timeStamp, String trackingNumber, List<OrderItem> orderItemList) {
        this.id = id;
        this.user = user;
        this.shoppingCart = shoppingCart;
        this.totalPrice = totalPrice;
        this.timeStamp = timeStamp;
        this.trackingNumber = trackingNumber;
        this.orderItemList = orderItemList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShoppingCart getCart() {
        return shoppingCart;
    }

    public void setCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", cart=" + shoppingCart +
                ", totalPrice=" + totalPrice +
                ", timeStamp=" + timeStamp +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
