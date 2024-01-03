package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.Order;
import com.selivanov.springmvcproject.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public void saveOrder(Order order) {
        BigDecimal totalPrice = order.getPrice()
                .multiply(BigDecimal.valueOf(order.getAmount()));
        order.setTotalPrice(totalPrice);
        orderRepository.saveOrder(order);
    }

    public void calculateTotalPrice(Order order) {
        BigDecimal totalPrice = order.getPrice()
                .multiply(BigDecimal.valueOf(order.getAmount()));
        order.setTotalPrice(totalPrice);
        orderRepository.saveOrder(order);
    }
    public void updateOrder(Integer id, Order order){
        orderRepository.updateOrder(order,id);
    }

    public void removeOrder (Integer id){
        if (id != null) {
            orderRepository.removeOrder(id);
        }
    }
}