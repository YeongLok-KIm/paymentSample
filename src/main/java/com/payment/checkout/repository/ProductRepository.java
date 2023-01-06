package com.payment.checkout.repository;

import com.payment.checkout.entity.Order;
import com.payment.checkout.entity.Payment;
import com.payment.checkout.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(int product_id);

    @Transactional
    Product save(Product product);
    
}
