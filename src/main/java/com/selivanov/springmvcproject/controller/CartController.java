package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.Cart;
import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.service.CartElementService;
import com.selivanov.springmvcproject.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CartController {
    private final CartService cartService;
    private final CartElementService cartElementService;

    @Autowired
    public CartController(CartService cartService, CartElementService cartElementService) {
        this.cartService = cartService;
        this.cartElementService = cartElementService;
    }

    @PostMapping("/clients/{name}/add-cart-element/{product_id}")
    public String cartClient(@PathVariable String name,
                             @PathVariable Integer product_id,
                             @ModelAttribute CartElement cartElement,
                             Model model) {

        cartService.addElementToCart(name, cartElement, product_id);
        model.addAttribute("cartElement", cartElement);
        return "redirect:/clients/%s/shop".formatted(name);
    }

    @GetMapping("/clients/{name}/cart")
    public String cartClient(@PathVariable String name,
                             Model model) {
        Cart cart = cartService.getCartIdByClientName(name); //
        List<CartElement> cartElements = cartService.getAllCartElementsByClientName(name);

        model.addAttribute("id", cart.getId());
        model.addAttribute("cartElements", cartElements);

        return "client/cart";
    }

}