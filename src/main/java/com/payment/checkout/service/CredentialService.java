package com.payment.checkout.service;

import com.payment.checkout.entity.Credential;
import com.payment.checkout.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CredentialService {
    @Autowired
    private CredentialRepository credentialRepository;

    public Credential findCredentailByImpCode(String imp_code){
        return this.credentialRepository.findCredentialByImpCode(imp_code);
    }

}
