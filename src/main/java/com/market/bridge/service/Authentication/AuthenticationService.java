package com.market.bridge.service.Authentication;

import com.market.bridge.dto.RegisterRequest;
import com.market.bridge.entity.enums.SystemRoles;
import com.market.bridge.entity.users.Buyer;
import com.market.bridge.entity.users.Seller;
import com.market.bridge.entity.users.UserEntity;
import com.market.bridge.repository.AdminRepo;
import com.market.bridge.repository.BuyerRepo;
import com.market.bridge.repository.SellerRepo;
import com.market.bridge.security.jwt.JwtService;
import com.market.bridge.service.UserDetailsService.BuyerDetailsService;
import com.market.bridge.service.UserDetailsService.ComposedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.market.bridge.dto.AuthenticationRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final ComposedUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final BuyerRepo buyerRepo;
    private final SellerRepo sellerRepo;
    private final AdminRepo adminRepo;

    public String buyerRegister(RegisterRequest request) {
        // Check if the username already exists
        if (buyerRepo.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Check if the email already exists
        if (buyerRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Buyer buyer = Buyer.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .roles(SystemRoles.BUYER.name())
                .build();

        // Save the buyer to the database
        buyerRepo.save(buyer);

        // Generate a JWT token for the buyer
        return jwtService.generateToken(new UserEntity(buyer));
    }

    public String sellerRegister(RegisterRequest request) {
        // Check if the username already exists
        if (sellerRepo.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        // Check if the email already exists
        if (sellerRepo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create a new seller object
        var seller = Seller.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .companyName(request.getCompanyName())
                .address(request.getAddress())
                .roles(SystemRoles.SELLER.toString())
                .build();

        // Save the seller to the database
        sellerRepo.save(seller);

        // Generate a JWT token for the seller
        return jwtService.generateToken(new UserEntity(seller));
    }

    public String authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return jwtService.generateToken(userDetails);
    }

}
