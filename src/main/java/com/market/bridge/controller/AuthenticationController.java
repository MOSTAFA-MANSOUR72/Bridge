package com.market.bridge.controller;

import com.market.bridge.dto.authentication.AuthenticationRequest;
import com.market.bridge.dto.authentication.RegisterRequest;
import com.market.bridge.exception.ValidationException;
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
        String result = switch (request.getRoles().toUpperCase()) {
            case "BUYER" -> authenticationService.buyerRegister(request);
            case "SELLER" -> authenticationService.sellerRegister(request);
            case "ADMIN" -> authenticationService.adminRegister(request);
            default -> throw new ValidationException("Invalid role: " + request.getRoles());
        };
        return ResponseEntity.ok(result);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(
                authenticationService.authenticate(request)
        );
    }
}
