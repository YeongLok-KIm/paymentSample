package com.payment.checkout.repository;

import com.payment.checkout.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findPaymentByImpUid(String imp_uid);

    Payment findPaymentByOrderId(int order_id);

    Payment findPaymentByMerchantUid(String merchnat_uid);

    @Transactional
    Payment save(Payment payment);

}
