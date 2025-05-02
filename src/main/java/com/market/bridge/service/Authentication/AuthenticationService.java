package com.market.bridge.service.Authentication;

import com.market.bridge.dto.authentication.AuthResponse;
import com.market.bridge.dto.authentication.AuthenticationRequest;
import com.market.bridge.dto.authentication.RegisterRequest;

public interface AuthenticationService {
    AuthResponse authenticate(AuthenticationRequest request);

    String buyerRegister(RegisterRequest request);

    String sellerRegister(RegisterRequest request);
}
