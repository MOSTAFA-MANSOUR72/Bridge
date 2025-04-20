package com.market.bridge.service.PasswordChange;

import com.market.bridge.entity.users.Admin;
import com.market.bridge.entity.users.Buyer;
import com.market.bridge.entity.users.Seller;
import com.market.bridge.repository.*;
import com.market.bridge.security.jwt.JwtService;
import com.market.bridge.service.MailService.MailService;
import com.market.bridge.service.UserDetailsService.ComposedUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final ComposedUserDetailsService userDetailsService;
    private final AdminRepo adminRepo;
    private final SellerRepo sellerRepo;
    private final BuyerRepo buyerRepo;

    public final MailService mailService;
    public final JwtService jwtService;
    public final PasswordEncoder passwordEncoder;
    @Override
    public void sendAuthToken(String gmail) {
        // Implementation to send authentication token to the provided email

        UserDetails userDetails =  userDetailsService.loadUserByEmail(gmail);
        if(userDetails == null) throw new UsernameNotFoundException("There is no user with the email: "+gmail);

        String token = jwtService.generateMailToken(userDetails);

        mailService.sendEmailAlert(
                gmail,
                "Bridge",
                "Your code is : \n"+ token +" \n"+
                        "Use it to verify your email and reset your password.\n"+
                        "Yours,\nThe Bridge Team"
        );
    }

    @Override
    public boolean validateAuthToken(String code) {
        // Implementation to validate the provided authentication token
        return jwtService.isTokenExpired(code);
    }

    @Override
    public void changePassword(String token, String newPassword) {
        // Implementation to change the password using the provided token and new password
        if(validateAuthToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        if(newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }
        if(newPassword.length() < 8) {
            throw new IllegalArgumentException("New password must be at least 8 characters long");
        }
        String pass = passwordEncoder.encode(newPassword);
        String username = jwtService.extractUsername(token);
        Admin admin = adminRepo.findByUsername(username).orElse(null);
        if(admin != null) {
            admin.setPassword(pass);
            adminRepo.save(admin);
            return;
        }

        Seller seller = sellerRepo.findByUsername(username).orElse(null);
        if(seller != null) {
            seller.setPassword(pass);
            sellerRepo.save(seller);
            return;
        }
        Buyer buyer = buyerRepo.findByUsername(username).orElse(null);
        if(buyer != null) {
            buyer.setPassword(pass);
            buyerRepo.save(buyer);
            return;
        }
        throw new IllegalArgumentException("User not found");
    }
}
