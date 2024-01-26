package com.selivanov.springmvcproject.service;

import com.selivanov.springmvcproject.entity.OrderItem;
import com.selivanov.springmvcproject.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllOrders() {
        return orderItemRepository.getAllOrderItems();
    }

    public OrderItem getOrderById(Integer id) {
        return orderItemRepository.getOrderItemById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "OrderItem with id = '%d' not found".formatted(id)));
    }

    public void saveOrderItem(OrderItem orderItem) {
        BigDecimal totalPrice = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getAmount()));
        orderItem.setTotalPrice(totalPrice);
        orderItemRepository.saveOrderItem(orderItem);
    }

    public void calculateTotalPrice(OrderItem orderItem) {
        BigDecimal totalPrice = orderItem.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getAmount()));
        orderItem.setTotalPrice(totalPrice);
        orderItemRepository.saveOrderItem(orderItem);
    }

    public void updateOrderItem(Integer id, OrderItem orderItem) {
        orderItemRepository.updateOrderItem(orderItem, id);
    }

    public void removeOrderItem(Integer id) {
        if (id != null) {
            orderItemRepository.removeOrderItem(id);
        }
    }
}