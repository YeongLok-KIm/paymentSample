package com.payment.checkout.service;

import com.payment.checkout.entity.User;
import com.payment.checkout.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserById(int user_id){
        return this.userRepository.findUserById(user_id);
    }

}
