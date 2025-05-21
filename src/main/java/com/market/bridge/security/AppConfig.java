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

import java.util.List;

@Configuration
@AllArgsConstructor
public class AppConfig {
    private final ComposedUserDetailsService composedUserDetailsService;
    private final AdminRepo adminRepo;
    private final SellerRepo sellerRepo;
    private final CategoryRepo categoryRepo;
    private final ProductRepo  productRepo;


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
            // For example, you can create default users or roles here
            // userService.createDefaultUsers();
            // roleService.createDefaultRoles();

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
 //            sellerRepo.save(seller);
             Admin admin = Admin.builder()
                     .roles("ADMIN")
                     .email("Admin@gmail.com")
                     .username("admin")
                     .password(passwordEncoder().encode("admin"))
                     .phoneNumber("1234567890")
                     .build();
             // Save the admin to the database
             adminRepo.save(admin);
             sellerRepo.save(seller);

             Category category = Category.builder()
                     .name("Electronics")
                     .parentCategoryId(null)
                     .build();

             // Save the category to the database
             categoryRepo.save(category);

             // Add products for the seller
            Product product1 = Product.builder()
                    .name("Laptop")
                    .description("High-performance laptop")
                    .price(1200.00)
                    .quantity((10L))
                    .categories(List.of(category))
                    .seller(seller)
                    .minOrder(2L)
                    .build();
            Product product2 = Product.builder()
                    .name("Smartphone")
                    .description("Latest model smartphone")
                    .price(800.00)
                    .quantity((20L))
                    .minOrder(2L)
                    .categories(List.of(category))
                    .seller(seller)
                    .build();
            Product product3 = Product.builder()
                    .name("Tablet")
                    .description("Portable tablet with great features")
                    .price(500.00)
                    .minOrder(2L)
                    .quantity((15L))
                    .categories(List.of(category))
                    .seller(seller)
                    .build();
            productRepo.saveAll(List.of(product1, product2, product3));
        };

    }

}
