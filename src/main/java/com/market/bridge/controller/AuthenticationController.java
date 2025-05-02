package com.market.bridge.controller;

import com.market.bridge.dto.authentication.AuthenticationRequest;
import com.market.bridge.dto.authentication.RegisterRequest;
import com.market.bridge.service.Authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return request.getRoles().equals("BUYER")?
         ResponseEntity.ok(
                authenticationService.buyerRegister(request)
        ):
        ResponseEntity.ok(
                authenticationService.sellerRegister(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(
                authenticationService.authenticate(request)
        );
    }
}
