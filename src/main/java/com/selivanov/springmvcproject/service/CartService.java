package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.Cart;
import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.entity.Client;
import com.selivanov.springmvcproject.repository.CartElementRepository;
import com.selivanov.springmvcproject.repository.CartRepository;
import com.selivanov.springmvcproject.repository.ClientRepository;
import com.selivanov.springmvcproject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final CartElementRepository cartElementRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, ClientRepository clientRepository, CartElementRepository cartElementRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.cartElementRepository = cartElementRepository;
    }

    public List<Cart> getAllCart() {
        return cartRepository.getAllCart();
    }

    public Cart getCart(Integer id) {
        return cartRepository.getCartById(id).orElseThrow(() -> new NoSuchElementException(
                "Cart with id = '%d' not found".formatted(id)));
    }

    public void saveCartElement(Cart cart) {
        cartRepository.saveCart(cart);
    }

    public void deleteCartElement(Integer id) {
        this.cartRepository.removeCart(id);
    }

    public void addElementToCart(String name, CartElement cartElement) {
        Client client = clientRepository.getClientByName(name).orElseThrow(() -> new NoSuchElementException(
                "Client with name = '%s' not found".formatted(name)));
        Cart cart = client.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setClient(client);
            cartRepository.saveCart(cart);
        }
        cartElement.setCart(cart);
        cartElementRepository.saveCartElement(cartElement);
    }

}
