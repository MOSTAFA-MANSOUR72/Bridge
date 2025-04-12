package com.market.bridge.controller;

import com.market.bridge.entity.users.Seller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class test {
    @GetMapping
    public String test(){
        var s = Seller.builder().roles("ldsaf").build();
        System.out.println(s.getRoles());
        return "test";
    }
}
