package com.market.bridge.service.Authentication;

import com.market.bridge.dto.AuthenticationRequest;
import com.market.bridge.dto.RegisterRequest;

public interface AuthenticationService {
    String authenticate(AuthenticationRequest request);

    String buyerRegister(RegisterRequest request);

    String sellerRegister(RegisterRequest request);
}
