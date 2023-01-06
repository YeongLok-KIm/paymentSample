package com.payment.checkout.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "name")
    private String name;

    public Product update_stock(int stock){
        this.stock = stock;
        return this;
    }

}
