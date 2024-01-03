package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

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