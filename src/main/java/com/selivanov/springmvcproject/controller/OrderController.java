package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.Cart;
import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.service.CartService;
import com.selivanov.springmvcproject.service.OrderItemService;
import com.selivanov.springmvcproject.service.OrderService;
import com.selivanov.springmvcproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final CartService cartService;


    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService, ProductService productService, CartService cartService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.cartService = cartService;
    }
        @GetMapping("/clients/{name}/cart/{id}/orders")
    public String getOrders(@PathVariable String name,
                                      @PathVariable Integer id,
                                      Model model) {
        return "client/orders";
    }
    @PostMapping("/clients/{name}/cart/{id}/orders")
    public String createOrderFromCart(@PathVariable String name,
                                      @PathVariable Integer id,
                                      Model model) {
        Cart cart = cartService.getCartIdByClientName(name);
        List<CartElement> cartElements = cartService.getAllCartElementsByClientName(name);
        model.addAttribute("cartElements", cartElements);
        model.addAttribute("id", cart.getId()); //
        return "redirect:/clients/%s/cart/%d/orders".formatted(name, id);
    }
//--
    @GetMapping("/orders/{id}/edit")
    public String getUpdateOrder(@PathVariable Integer id, Model model) {
        if (id != null) {
            Order order = orderService.getOrderById(id);
            model.addAttribute("order", order);
        }

        return "client/update_order";
    }

    @PutMapping("/orders/{id}")
    public String updateEmployee(@PathVariable Integer id,
                                 @ModelAttribute Order order) {
        orderService.updateOrder(id, order);
        return "redirect:/clients";
    }

    @DeleteMapping("orders/{id}")
    public String removeOrder(@PathVariable Integer id) {
        if (id != null) {
            orderService.removeOrder(id);
        }
        return "redirect:/clients";
    }

}