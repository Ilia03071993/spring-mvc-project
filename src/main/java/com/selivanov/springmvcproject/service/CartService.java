package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.Cart;
import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.entity.Client;
import com.selivanov.springmvcproject.entity.Product;
import com.selivanov.springmvcproject.repository.CartElementRepository;
import com.selivanov.springmvcproject.repository.CartRepository;
import com.selivanov.springmvcproject.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ClientService clientService;
    private final ProductService productService;
    private final CartElementService cartElementService;

    @Autowired
    public CartService(CartRepository cartRepository, ClientService clientService, ProductService productService, CartElementService cartElementService) {
        this.cartRepository = cartRepository;
        this.clientService = clientService;
        this.productService = productService;
        this.cartElementService = cartElementService;
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

    public void addElementToCart(String name, CartElement cartElement, Integer productId) {
        Client client = clientService.getClientWithCart(name);

        Cart cart = client.getCart();
        cart.addCartElement(cartElement);

        Product product = productService.getProduct(productId);
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(cartElement.getAmount()));
        cartElement.setPrice(totalPrice);
        cartElement.setProduct(product);

        cartRepository.saveCart(cart);
    }

    public List<CartElement> getAllCartElementsByClientName(String name) {
        return cartRepository.getAllCartElementsByClientName(name);
    }
}