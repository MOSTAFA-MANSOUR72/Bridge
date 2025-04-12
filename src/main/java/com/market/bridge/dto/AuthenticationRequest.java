package com.market.bridge.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    public String username;
    public String password;
}
