package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.repository.CartRepository;
import com.selivanov.springmvcproject.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/clients/{name}/cart-element")
    public String cartClient(@PathVariable String name, CartElement cartElement, Model model) {
        cartService.addElementToCart(name, cartElement);
        model.addAttribute("cartElement", cartElement);
        return "client/cart_element";
    }
}
