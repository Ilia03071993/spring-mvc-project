package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartService cartService;

    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService, CartService cartService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
    }

    @GetMapping("/clients/{name}/orders")
    public String getOrders(@PathVariable String name,
                            Model model) {
        List<Order> orders = orderService.getOrdersByClientName(name);
        BigDecimal totalPrice = orderItemService.totalPriceItems(name);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orders", orders);
        return "client/orders";
    }

    @PostMapping("/clients/{name}/orders")
    public String createOrderFromCart(@PathVariable String name) {
        cartService.createOrderFromCart(name);
        return "redirect:/clients/%s/orders".formatted(name);
    }

    @DeleteMapping("/clients/{name}/orders/{id}")
    public String removeOrder(@PathVariable String name, @PathVariable Integer id) {
        orderService.removeOrder(id);
        return "redirect:/clients/%s/orders".formatted(name);
    }
}
//--
//    @GetMapping("/orders/{id}/edit")
//    public String getUpdateOrder(@PathVariable Integer id, Model model) {
//        if (id != null) {
//            Order order = orderService.getOrderById(id);
//            model.addAttribute("order", order);
//        }
//
//        return "client/update_order";
//    }
//
//    @PutMapping("/orders/{id}")
//    public String updateEmployee(@PathVariable Integer id,
//                                 @ModelAttribute Order order) {
//        orderService.updateOrder(id, order);
//        return "redirect:/clients";
//    }
//
//    @DeleteMapping("orders/{id}")
//    public String removeOrder(@PathVariable Integer id) {
//        if (id != null) {
//            orderService.removeOrder(id);
//        }
//        return "redirect:/clients";
//    }