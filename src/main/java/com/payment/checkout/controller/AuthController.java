package com.payment.checkout.controller;


import com.payment.checkout.entity.*;
import com.payment.checkout.service.*;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Certification;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    CredentialService credentialService;

    @Value("${imp_code}")
    String imp_code;

    @GetMapping("/call")
    public String call_auth(Model model) {

        try {

            long nano = System.currentTimeMillis();
            String merchant_uid = "muid-"+nano;

            model.addAttribute("imp_code", this.imp_code);
            model.addAttribute("merchant_uid", merchant_uid);

        }catch(Exception e){
            e.printStackTrace();
            return "error";
        }

        return "authorization";

    }

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
            responseObj.put("status", "인증실패 : 관리자에게 문의해 주세요.");

        }

        return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value="/redirect", method = RequestMethod.POST)
    public ResponseEntity<?> redirect_receive(@RequestBody Iamport iamportEntity) {

        // 응답 header 생성
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        JSONObject responseObj = new JSONObject();

        try {
            String imp_uid = iamportEntity.getImp_uid();
            String merchant_uid = iamportEntity.getMerchant_uid();

            System.out.println("---redirect receive---");
            System.out.println("----------------------");
            System.out.println("imp_uid : " + imp_uid);
            System.out.println("merchant_uid : " + merchant_uid);


            String status = doResult(iamportEntity);
            responseObj.put("status", status);

        } catch (Exception e) {
            e.printStackTrace();
            responseObj.put("status", "인증실패 : 관리자에게 문의해 주세요.");

        }

        return new ResponseEntity<String>(responseObj.toString(), responseHeaders, HttpStatus.OK);
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
            System.out.println("error_code : " + error_code);
            System.out.println("error_msg : " + error_msg);

            String status = doResult(iamportEntity);
            responseObj.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
            responseObj.put("status", "인증실패 : 관리자에게 문의해 주세요.");

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

                Credential credential = this.credentialService.findCredentailByImpCode(this.imp_code);
                String api_key = credential.getApiKey();
                String api_secret = credential.getApiSecret();

                IamportClient ic = new IamportClient(api_key, api_secret);
                IamportResponse<Certification> response = ic.certificationByImpUid(imp_uid);
                Certification apiResponse = response.getResponse();
                System.out.println("생년월일: " + apiResponse.getBirth());
                System.out.println("성별: " + apiResponse.getGender());
                System.out.println("이름: " + apiResponse.getName());
                System.out.println("CI: " + apiResponse.getUniqueKey());
                System.out.println("DI: " + apiResponse.getUniqueInSite());


                status = apiResponse.getName()+" : "+response.getResponse().getUniqueKey();
            }

        } catch (Exception e) {
            e.printStackTrace();
            status = "인증실패 : 관리자에게 문의해 주세요.";

        }

        return status;
    }


}
