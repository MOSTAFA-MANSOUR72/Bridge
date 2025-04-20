package com.market.bridge.controller;


import com.market.bridge.dto.ChangeRequest;
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

    @GetMapping("/send/{gmail}")
    public ResponseEntity<String> send(@PathVariable String gmail) {
        try {
            passwordService.sendAuthToken(gmail);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            logger.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error sending email: " + gmail + " " + e.getMessage());
        }
    }

    @PostMapping("/change")
    public ResponseEntity<String> change(@RequestBody ChangeRequest request) {
        try {
            passwordService.changePassword(request.getToken(), request.getPassword());
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            logger.error("Error changing password: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error changing password: " + e.getMessage());
        }
    }

}
