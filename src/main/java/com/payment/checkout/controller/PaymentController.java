package com.payment.checkout.controller;

import com.payment.checkout.entity.Credential;
import com.payment.checkout.entity.Order;
import com.payment.checkout.entity.Iamport;
import com.payment.checkout.entity.Product;
import com.payment.checkout.entity.Payment;
import com.payment.checkout.service.CredentialService;
import com.payment.checkout.service.OrderService;
import com.payment.checkout.service.PaymentService;
import com.payment.checkout.service.ProductService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    CredentialService credentialService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @Value("${imp_code}")
    String imp_code;

    // 콜백 수신 처리
    @RequestMapping(value="/callback", method = RequestMethod.POST)
    public ResponseEntity<?> callback_receive(@RequestBody Iamport iamportEntity) {

        // 응답 header 생성
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        JSONObject responseObj = new JSONObject();

        try {
            String imp_uid = iamportEntity.getImp_uid();
            String merchant_uid = iamportEntity.getMerchant_uid();
            String error_code = iamportEntity.getError_code();
            String error_msg = iamportEntity.getError_msg();

            System.out.println("---callback receive---");
            System.out.println("----------------------");
            System.out.println("imp_uid : " + imp_uid);
            System.out.println("merchant_uid : " + merchant_uid);
            System.out.println("error_code : " + error_code);
            System.out.println("error_msg : " + error_msg);

            String status = doResult(iamportEntity);
            responseObj.put("status", status);

        } catch (Exception e) {
            e.printStackTrace();
            responseObj.put("status", "결제실패 : 관리자에게 문의해 주세요.");

        }

        return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
    }

    // get redirect
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public ModelAndView redirect_receive(@RequestParam(value = "imp_uid", required = false) String imp_uid,
                                         @RequestParam(value = "merchant_uid", required = false) String merchant_uid) {

        String process_result = "redirection!";
        // 응답 header 생성
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        JSONObject responseObj = new JSONObject();
        ModelAndView mav = new ModelAndView();

        try {

            System.out.println("---redirect---");
            System.out.println("imp_uid : " + imp_uid);
            System.out.println("merchant_uid : " + merchant_uid);

            Iamport iamportEntity = new Iamport();
            iamportEntity.setImp_uid(imp_uid);
            iamportEntity.setMerchant_uid(merchant_uid);
            String status = doResult(iamportEntity);
            mav.addObject("status", status);
            mav.setViewName("redirect");

        } catch (Exception e) {
            e.printStackTrace();
            responseObj.put("process_result", "결제실패 : 관리자에게 문의해 주세요.");

        }

        return mav;
    }

    // 웹훅 수신 처리
    @RequestMapping(value="/webhook", method = RequestMethod.POST)
    public ResponseEntity<?> webhook_receive(@RequestBody Iamport iamportEntity) {

        // 응답 header 생성
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        JSONObject responseObj = new JSONObject();

        try {
            String imp_uid = iamportEntity.getImp_uid();
            String merchant_uid = iamportEntity.getMerchant_uid();
            String error_code = iamportEntity.getError_code();
            String error_msg = iamportEntity.getError_msg();

            System.out.println("---webhook receive---");
            System.out.println("imp_uid : " + imp_uid);
            System.out.println("merchant_uid : " + merchant_uid);

            String status = doResult(iamportEntity);

        } catch (Exception e) {
            e.printStackTrace();
            responseObj.put("status", "결제실패 : 관리자에게 문의해 주세요.");

        }

        return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
    }

    //공통처리
    private String doResult(Iamport iamportEntity) {

        String status = "";

        try {
            String imp_uid = iamportEntity.getImp_uid();
            String merchant_uid = iamportEntity.getMerchant_uid();
            String error_code = iamportEntity.getError_code();
            String error_msg = iamportEntity.getError_msg();


            if (imp_uid != null) {

                //콜백 또는 m_redirect_url로 결과가 이미 반영되어 있는지 검사
                Payment check_payment = this.paymentService.findPaymentByImpUid(imp_uid);
                if(check_payment !=null){
                    return check_payment.getStatus();
                }

                // STEP 5
                Credential credential = this.credentialService.findCredentailByImpCode(this.imp_code);
                String api_key = credential.getApiKey();
                String api_secret = credential.getApiSecret();

                IamportClient ic = new IamportClient(api_key, api_secret);
                IamportResponse<com.siot.IamportRestClient.response.Payment> response = ic.paymentByImpUid(imp_uid);
                com.siot.IamportRestClient.response.Payment apiResponse = response.getResponse();
                BigDecimal iamport_amount = apiResponse.getAmount();
                //custom_data에 주문테이블 pk를 실었다가 읽는다.
                String order_id = apiResponse.getCustomData();

                Order order = this.orderService.findOrderById(Integer.parseInt(order_id));
                Product product = this.productService.findProductById(order.getProductId());

                //결제데이터 생성
                Payment payment = Payment.builder()
                        .userId(order.getUserId())
                        .orderId(order.getId())
                        .amount(iamport_amount)
                        .pgCode(apiResponse.getPgProvider())
                        .pgTid(apiResponse.getPgTid())
                        .applyNum(apiResponse.getApplyNum())
                        .buyerEmail(apiResponse.getBuyerEmail())
                        .buyerAddr(apiResponse.getBuyerAddr())
                        .buyerPostcode(apiResponse.getBuyerPostcode())
                        .cardCode(apiResponse.getCardCode())
                        .cardQuota(apiResponse.getCardQuota())
                        .currency(apiResponse.getCurrency())
                        .impUid(imp_uid)
                        .merchantUid(merchant_uid)
                        .payMethod(apiResponse.getPayMethod())
                        .customData(apiResponse.getCustomData())
                        .isEscrow(apiResponse.isEscrow())
                        .status(apiResponse.getStatus())
                        .vbankDate(apiResponse.getVbankDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .vbankNum(apiResponse.getVbankNum())
                        .vbankCode(apiResponse.getVbankCode()).build();

                this.paymentService.save(payment);

                //STEP5
                //주문요청 금액과 실제 결제금액이 같은지 비교
                if(order.getAmount().equals(iamport_amount)) {

                    if ("cacelled".equals(apiResponse.getStatus())){
                        //주문상태 변경
                        order.update_status("결제취소", "결과수신시 취소로 수신");
                        //재고 원복
                        product.update_stock(product.getStock()+1);
                        this.productService.save(product);

                    }else if ("failed".equals(apiResponse.getStatus())){
                        //주문상태 변경
                        order.update_status("결제실패", apiResponse.getFailReason());
                        //재고 원복
                        product.update_stock(product.getStock()+1);
                        this.productService.save(product);
                    }
                    //주문상태 변경
                    order.update_status("주문성공", "결제성공/가상계좌발급성공");
                    this.orderService.save(order);

                }else{
                    //금액 위변조 취소처리
                    System.out.println("금액위변조 취소처리:"+order.getAmount()+"-"+iamport_amount);
                    CancelData cancelData = new CancelData(imp_uid, true);
                    IamportResponse<com.siot.IamportRestClient.response.Payment> cancel_response = ic.cancelPaymentByImpUid(cancelData);
                    if(cancel_response.getCode()==0){
                        payment.update_cancel("cancelled", "금액 위변조",  LocalDateTime.now() );
                    }else{
                        payment.update_fail("check", "취소실패!! 확인필요",  LocalDateTime.now() );
                    }
                    this.paymentService.save(payment);

                    //주문상태 변경
                    order.update_status("결제실패", apiResponse.getFailReason());
                    //재고 원복
                    product.update_stock(product.getStock()+1);
                    this.productService.save(product);

                }

                status = apiResponse.getStatus();


            } else {
                System.out.println("error_msg : " + error_msg);
                status = "결제실패 : " + error_msg;
            }

        } catch (Exception e) {
            e.printStackTrace();
            status = "결제실패 : 관리자에게 문의해 주세요.";

        }

        return status;
    }

}
