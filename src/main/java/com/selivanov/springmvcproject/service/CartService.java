package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.*;
import com.selivanov.springmvcproject.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ClientService clientService;
    private final ProductService productService;
    private final CartElementService cartElementService;
    private final OrderService orderService;

    @Autowired
    public CartService(CartRepository cartRepository, ClientService clientService, ProductService productService, CartElementService cartElementService, OrderService orderService) {
        this.cartRepository = cartRepository;
        this.clientService = clientService;
        this.productService = productService;
        this.cartElementService = cartElementService;
        this.orderService = orderService;
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

    public Integer getCartIdByClientName(String name) {
        return cartRepository.getCartIdByClientName(name).orElseThrow(() -> new NoSuchElementException(
                "Cart with client name = '%s' not found".formatted(name)));
    }

    public void createOrderFromCart(String clientName) {
        List<CartElement> cartElements = getAllCartElementsByClientName(clientName);
        Order ord = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        if (!cartElements.isEmpty()) {
            for (CartElement cartElement : cartElements) {
                OrderItem item = new OrderItem();
                item.setOrder(ord);
                item.setPrice(cartElement.getPrice());
                item.setAmount(cartElement.getAmount());
                item.setProduct(cartElement.getProduct());
                item.setTotalPrice(cartElement.getPrice().multiply(BigDecimal.valueOf(cartElement.getAmount())));
                orderItems.add(item);
            }

            ord.setClient(clientService.getClientByName(clientName));
            ord.setOrderItemList(orderItems);
        }
        orderService.saveOrder(ord);
    }
}