package com.payment.checkout.service;

import com.payment.checkout.entity.Product;
import com.payment.checkout.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product findProductById(int product_id){
        return this.productRepository.findProductById(product_id);
    }

    public Product save(Product product){
        return this.productRepository.save(product);
    }

}
