package com.market.bridge.dto.authentication;

import com.market.bridge.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Please provide a username")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @Email(message = "Please provide a valid email address")
    private String email;
    private String phoneNumber;
    private Address address;
    @NotBlank
    private String roles;
    private String companyName;
}
