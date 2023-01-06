package com.payment.checkout.service;

import com.payment.checkout.entity.Order;
import com.payment.checkout.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order findOrderById(int order_id){
        return this.orderRepository.findOrderById(order_id);
    }

    public Order save(Order order){return this.orderRepository.save(order);}


}
