package com.market.bridge.dto;

import com.market.bridge.entity.Address;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Address address;
    private String roles;

    private String companyName;
}
