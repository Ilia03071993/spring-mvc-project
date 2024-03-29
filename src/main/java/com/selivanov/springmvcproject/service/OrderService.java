package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.*;
import com.selivanov.springmvcproject.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderById(Integer id) {
        return orderRepository.getOrderById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Order with id = '%d' not found".formatted(id)));
    }

    public List<Order> getOrdersByClientName(String name) {
        return orderRepository.getAllOrdersByClientName(name);
    }

    public void saveOrder(Order order) {
        orderRepository.saveOrder(order);
    }

    public void updateOrder(Integer id, Order order) {
        orderRepository.updateOrder(order, id);
    }

    public void removeOrder(Integer id) {
        if (id != null) {
            orderRepository.removeOrder(id);
        }
    }

}