package com.selivanov.springmvcproject.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Client client;
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartElement> cartElementList = new ArrayList<>();

    public Cart() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        client.setCart(this);
        this.client = client;
    }

    public List<CartElement> getCartElementList() {
        return cartElementList;
    }

    public void setCartElementList(List<CartElement> cartElementList) {
        for (CartElement cartElement : cartElementList) {
            cartElement.setCart(this);
        }
        this.cartElementList = cartElementList;
    }

    public void addCartElement(CartElement cartElement) {
        cartElement.setCart(this);
        cartElementList.add(cartElement);
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", client=" + client +
                ", cartElementList=" + cartElementList +
                '}';
    }


}