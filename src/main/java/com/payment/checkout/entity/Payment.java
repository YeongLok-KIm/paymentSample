package com.payment.checkout.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "cancel_amount")
    private BigDecimal cancelAmount;

    @Column(name = "imp_uid")
    private String impUid;

    @Column(name = "merchant_uid")
    private String merchantUid;

    @Column(name = "pay_method", nullable = false)
    private String payMethod;

    @Column(name = "pg_tid")
    private String pgTid;
    @Column(name = "pg_code")
    private String pgCode;

    @Column(name = "apply_num")
    private String applyNum;

    @Column(name = "card_code")
    private String cardCode;

    @Column(name = "vbank_num")
    private String vbankNum;

    @Column(name = "vbank_date")
    private LocalDateTime vbankDate;

    @Column(name = "vbank_code")
    private String vbankCode;

    @Column(name = "is_escrow")
    private Boolean isEscrow;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_email")
    private String buyerEmail;

    @Column(name = "buyer_tel")
    private String buyerTel;

    @Column(name = "buyerAddr")
    private String buyerAddr;

    @Column(name = "buyer_postcode")
    private String buyerPostcode;

    @Column(name = "custom_data")
    private String customData;

    @Column(name = "card_quota")
    private int cardQuota;
    @Column(name = "status")
    private String status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "failed_at")
    private LocalDateTime failedAt;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(name = "modified", nullable = false)
    private LocalDateTime modified;

    public Payment update_success(String status, String pg_tid, String apply_num, LocalDateTime paid_at){
        this.status = status;
        this.pgTid = pg_tid;
        this.applyNum = apply_num;
        this.paidAt = paidAt;
        return this;
    }

    public Payment update_fail(String status, String fail_reason, LocalDateTime failed_at){
        this.status = status;
        this.failReason = fail_reason;
        this.failedAt = failed_at;
        return this;
    }

    public Payment update_cancel(String status, String cancel_reason, LocalDateTime canlled_at){
        this.status = status;
        this.cancelReason = cancel_reason;
        this.cancelledAt = canlled_at;
        return this;
    }

    @Builder
    public Payment(
            String imp_uid,
            String pg_provider,
            String pg_code,
            String pay_method,
            Boolean is_escrow,
            int user_id,
            int order_id,
            String currency,
            BigDecimal amount,
            BigDecimal vat,
            int card_quota,
            String merchant_uid,
            String buyer_name,
            String buyer_email,
            String buyer_tel,
            String buyer_addr,
            String buyer_postcode,
            String custom_data,
            LocalDateTime vbank_date,
            String vbank_code,
            String vbank_num,
            String status

    ){
        this.impUid = imp_uid;
        this.pgCode = pg_code;
        this.payMethod = pay_method;
        this.isEscrow = is_escrow;
        this.userId = user_id;
        this.orderId = order_id;
        this.currency = currency;
        this.amount = amount;
        this.vat = vat;
        this.cardQuota = card_quota;
        this.merchantUid = merchant_uid;
        this.buyerName = buyer_name;
        this.buyerEmail = buyer_email;
        this.buyerTel = buyer_tel;
        this.buyerAddr = buyer_addr;
        this.buyerPostcode = buyer_postcode;
        this.customData = custom_data;
        this.vbankDate = vbank_date;
        this.vbankCode = vbank_code;
        this.vbankNum = vbank_num;
        this.status = status;

    }

}
