package com.selivanov.springmvcproject.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @Column(nullable = false)
//    private String product;

    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer amount;
    private BigDecimal totalPrice; //price * amount
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "products")
    private List<Product> products;

    public Order(BigDecimal price, Integer amount, BigDecimal totalPrice) {
        this.price = price;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        for (Product product : products) {
            product.getOrders().add(this);
        }
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
