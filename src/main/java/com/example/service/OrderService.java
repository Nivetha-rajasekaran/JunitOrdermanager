package com.example.service;

import java.util.List;

import com.example.entity.Order;

public interface OrderService {
    String addOrder(Order order);
    String deleteOrder(int orderId);
    List<Order> getAllOrders();
}
