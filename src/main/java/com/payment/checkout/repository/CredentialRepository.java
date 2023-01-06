package com.payment.checkout.repository;

import com.payment.checkout.entity.Credential;
import com.payment.checkout.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Credential findCredentialByImpCode(String imp_code);
    
}
