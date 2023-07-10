package com.cognixia.jump.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

@Entity
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(nullable=false)
    private String name;

    @Min(0)
    @Max(25)
    @NotBlank
    @Column(nullable = false)
    private Integer stockAmount;

    private String description;

    private String imageURL;

    @NotNull
    @Column(columnDefinition="double default 0.00")
    private Double price;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    List<OrderItem> orderItemList;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    List<ShoppingCart> cartList;

    public Item(){

    }

    public Item(Integer id, String name, Integer stockAmount, String description, String imageURL, Double price, List<OrderItem> orderItemList,
                List<ShoppingCart> cartList) {
        this.id = id;
        this.name = name;
        this.stockAmount = stockAmount;
        this.description = description;
        this.imageURL = imageURL;
        this.price = price;
        this.orderItemList = orderItemList;
        this.cartList = cartList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Integer stockAmount) {
        this.stockAmount = stockAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public List<ShoppingCart> getCartList() {
        return cartList;
    }

    public void setCartList(List<ShoppingCart> cartList) {
        this.cartList = cartList;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stockAmount=" + stockAmount +
                ", description='" + description + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", price=" + price +
                ", orderItemList=" + orderItemList +
                ", cartList=" + cartList +
                '}';
    }
}
