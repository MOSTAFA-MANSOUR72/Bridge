package com.market.bridge.dto.authentication;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

@Data
public class AuthResponse {
    private String token;
    private String username;
    private List<String> roles;

    public AuthResponse(String token, UserDetails userDetails){
        this.token = token;
        this.username = userDetails.getUsername();
        this.roles = userDetails.getAuthorities().stream()
                .map( a -> a.getAuthority())
                .toList();
    }
}
