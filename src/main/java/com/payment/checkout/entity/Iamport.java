package com.payment.checkout.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Iamport {
    public String imp_uid;
    public String merchant_uid;
    public String error_code;
    public String error_msg;
}
