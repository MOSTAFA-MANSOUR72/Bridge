package com.market.bridge.dto.authentication;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
public class ChangeRequest {
    String token;
    String password;
}
