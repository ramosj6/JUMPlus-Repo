package com.cognixia.jump.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

@Entity
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn( name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn( name = "item_id", referencedColumnName = "id")
    @JsonIgnore
    private Item item;

    @NotBlank
    @Column(columnDefinition = "integer default 0")
    private Integer count;

    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    List<Order> orderList;

    public ShoppingCart(){

    }

    public ShoppingCart(Integer id, User user, Item item, Integer count, List<Order> orderList) {
        this.id = id;
        this.user = user;
        this.item = item;
        this.count = count;
        this.orderList = orderList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", user=" + user +
                ", item=" + item +
                ", count=" + count +
                ", orderList=" + orderList +
                '}';
    }
}
