package com.market.bridge.controller;


import com.market.bridge.dto.authentication.ChangeRequest;
import com.market.bridge.service.PasswordChange.PasswordService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@AllArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;


    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    @GetMapping("/send/{Gmail}")
    public ResponseEntity<String> send(@PathVariable String gmail) {
        passwordService.sendAuthToken(gmail);
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/change")
    public ResponseEntity<String> change(@RequestBody ChangeRequest request) {
        passwordService.changePassword(request.getToken(), request.getPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}
