package com.market.bridge.security;

import com.itextpdf.text.pdf.BidiLine;
import com.market.bridge.entity.Address;
import com.market.bridge.entity.Category;
import com.market.bridge.entity.Product;
import com.market.bridge.entity.users.Admin;
import com.market.bridge.entity.users.Seller;
import com.market.bridge.repository.*;
import com.market.bridge.service.UserDetailsService.ComposedUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AppConfig {
    private final ComposedUserDetailsService composedUserDetailsService;
    private final AdminRepo adminRepo;
    private final SellerRepo sellerRepo;
    private final CategoryRepo categoryRepo;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(composedUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CommandLineRunner init(){
        return args -> {
            // Initialize the application with necessary data or configurations

            Address address = new Address();
            address.setStreet("123 Test Street");
            address.setCity("Test City");
            address.setState("Test State");
            address.setZipCode("12345");
            address.setCountry("Test Country");

            // Create a Seller object
            Seller seller = Seller.builder()
                    .username("user")
                    .password(passwordEncoder().encode("password"))
                    .email("test_seller@example.com")
                    .phoneNumber("1234567890")
                    .roles("SELLER")
                    .address(address)
                    .build();
             Admin admin = Admin.builder()
                     .roles("ADMIN")
                     .email("Admin@gmail.com")
                     .username("admin")
                     .password(passwordEncoder().encode("admin"))
                     .phoneNumber("1234567890")
                     .build();
             // Save
             adminRepo.save(admin);
             sellerRepo.save(seller);

             Category category = Category.builder()
                     .name("Electronics")
                     .parentCategoryId(null)
                     .build();

             // Save the category to the database
             categoryRepo.save(category);

        };

    }

}
