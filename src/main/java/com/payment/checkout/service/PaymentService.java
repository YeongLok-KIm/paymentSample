package com.payment.checkout.service;

import com.payment.checkout.entity.Payment;
import com.payment.checkout.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment findPaymentByImpUid(String imp_uid){
        return this.paymentRepository.findPaymentByImpUid(imp_uid);
    }


    public Payment findPaymentByOrderId(int order_id){
        return this.paymentRepository.findPaymentByOrderId(order_id);
    }

    public Payment findPaymentByMerchantUid(String merchnat_uid){
        return this.paymentRepository.findPaymentByMerchantUid(merchnat_uid);
    }

    public Payment save(Payment payment){
        return this.paymentRepository.save(payment);
    }

}
