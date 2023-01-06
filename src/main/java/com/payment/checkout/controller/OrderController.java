package com.payment.checkout.controller;


import com.payment.checkout.entity.Product;
import com.payment.checkout.entity.User;
import com.payment.checkout.entity.Order;
import com.payment.checkout.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class OrderController {
    @Autowired
    ProductService productService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Value("${imp_code}")
    String imp_code;

    @GetMapping("/order/{product_id}")
    public String order(@PathVariable(value = "product_id") int product_id, Model model) {

        try {

            System.out.println("product_id : " + product_id);
            Product product = this.productService.findProductById(product_id);

            if(product == null){
                System.out.println("상품을 찾을 수 없습니다.");
                model.addAttribute("error_msg", "상품을 찾을 수 없습니다.");
                return "error";
            }

            //로그인 된 user 의 아이디가 1이라고 할 때
            User user = this.userService.findUserById(1);

            //상품 주문정보 생성(주문시도 상태가 30분 이상 지난 건은 재고를 다시 원복한다.)
            Order order = Order.builder()
                    .productId(product_id)
                    .amount(product.getAmount())
                    .userId(user.getId())
                    .status("주문시도")
                    .build();

            order = this.orderService.save(order);

            if(product.getStock() == 0){
                System.out.println("상품재고가 소진되었습니다.");
                order.update_status("주문실패", "재고소진");
                this.orderService.save(order);
                model.addAttribute("error_msg", "상품재고가 소진되었습니다.");
                return "error";
            }else{
                //재고를 감소시켜 놓는다.
                product.update_stock(product.getStock()-1);
                this.orderService.save(order);
            }

            long nano = System.currentTimeMillis();
            String merchant_uid = "muid-"+nano;

            //멀티PG 분기(db로 관리 할 수 있다.)
            String[] pg_code = {"html5_inicis", "kcp", "nice", "tosspayments"};
            int seleted_pg = (int) nano % pg_code.length;

            model.addAttribute("imp_code", this.imp_code);
            model.addAttribute("pg_code", pg_code[seleted_pg]);
            model.addAttribute("merchant_uid", merchant_uid);
            model.addAttribute("user", user);
            model.addAttribute("product", product);
            model.addAttribute("order", order);


        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }

        return "checkout";

    }

    @GetMapping("/overseas_order/{product_id}")
    public String overseas_order(@PathVariable(value = "product_id") int product_id, Model model) {

        try {

            System.out.println("product_id : " + product_id);
            Product product = this.productService.findProductById(product_id);

            if(product == null){
                System.out.println("상품을 찾을 수 없습니다.");
                model.addAttribute("error_msg", "상품을 찾을 수 없습니다.");
                return "error";
            }

            //로그인 된 user 의 아이디가 1이라고 할 때
            User user = this.userService.findUserById(1);

            //상품 주문정보 생성(주문시도 상태가 30분 이상 지난 건은 재고를 다시 원복한다.)
            Order order = Order.builder()
                    .productId(product_id)
                    .amount(product.getAmount())
                    .userId(user.getId())
                    .status("주문시도")
                    .build();

            order = this.orderService.save(order);

            if(product.getStock() == 0){
                System.out.println("상품재고가 소진되었습니다.");
                order.update_status("주문실패", "재고소진");
                this.orderService.save(order);
                model.addAttribute("error_msg", "상품재고가 소진되었습니다.");
                return "error";
            }else{
                //재고를 감소시켜 놓는다.
                product.update_stock(product.getStock()-1);
                this.orderService.save(order);
            }

            long nano = System.currentTimeMillis();
            String merchant_uid = "muid-"+nano;


            model.addAttribute("imp_code", this.imp_code);
            model.addAttribute("merchant_uid", merchant_uid);
            model.addAttribute("user", user);
            model.addAttribute("product", product);
            model.addAttribute("order", order);


        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }

        return "overseas";

    }

}
