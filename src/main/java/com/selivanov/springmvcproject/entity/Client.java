package com.selivanov.springmvcproject.entity;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    //    @OneToMany(cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY)
//    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Client() {
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        for (Order order : orders) {
            order.setClient(this);
        }
        this.orders = orders;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void updateOrder(Integer orderId, Order order) {
        Order update = orders.get(orderId);
        if (update != null) {
            update.setProduct(order.getProduct());
            update.setAmount(order.getAmount());
            update.setPrice(order.getPrice());
        }
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}
