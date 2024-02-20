package com.selivanov.springmvcproject.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList = new ArrayList<>();
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public Order(String street) {
        this.street = street;
    }

    public Order() {
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrder(this);
        }
        this.orderItemList = orderItemList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", orderItemList=" + orderItemList +
                ", client=" + client +
                '}';
    }
}
