package com.payment.checkout.repository;

import com.payment.checkout.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderById(int order_id);

    @Transactional
    Order save(Order order);
    
}
