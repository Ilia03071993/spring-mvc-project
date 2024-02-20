package com.selivanov.springmvcproject.controller;

import com.selivanov.springmvcproject.entity.CartElement;
import com.selivanov.springmvcproject.entity.Client;
import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.entity.OrderItem;
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
    private final ProductService productService;
    private final CartService cartService;
    private final ClientService clientService;


    @Autowired
    public OrderController(OrderService orderService, OrderItemService orderItemService, ProductService productService, CartService cartService, ClientService clientService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.cartService = cartService;
        this.clientService = clientService;
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
    public String createOrderFromCart(@PathVariable String name,
                                      @ModelAttribute OrderItem orderItem,
                                      @ModelAttribute Order order) {
        List<CartElement> cartElements = cartService.getAllCartElementsByClientName(name);
        Order ord = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartElement cartElement : cartElements){
            OrderItem item = new OrderItem();
            item.setOrder(ord);
            item.setPrice(cartElement.getPrice());
            item.setAmount(cartElement.getAmount());
            item.setProduct(cartElement.getProduct());
            item.setTotalPrice(cartElement.getPrice().multiply(BigDecimal.valueOf(cartElement.getAmount())));
            orderItems.add(item);
        }
        ord.setClient(clientService.getClientByName(name));
        ord.setOrderItemList(orderItems);
        orderService.saveOrder(ord);

        //save order

        return "redirect:/clients/%s/orders".formatted(name);
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

}