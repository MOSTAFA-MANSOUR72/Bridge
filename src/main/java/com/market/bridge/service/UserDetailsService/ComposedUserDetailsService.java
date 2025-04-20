package com.market.bridge.service.UserDetailsService;

import com.market.bridge.service.UserDetailsService.AdminDetailsService;
import com.market.bridge.service.UserDetailsService.BuyerDetailsService;
import com.market.bridge.service.UserDetailsService.SellerDetailsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Data
public class ComposedUserDetailsService implements UserDetailsService {
    private final SellerDetailsService sellerDetailsService;
    private final BuyerDetailsService buyerDetailsService;
    private final AdminDetailsService adminDetailsService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for(var userDetailsService :
                new UserDetailsService[]{sellerDetailsService, buyerDetailsService, adminDetailsService}) {
            try {
                UserDetails userDetails =  userDetailsService.loadUserByUsername(username);
                return userDetails;
            } catch (UsernameNotFoundException ignored) {
                // Ignore and try the next service
            }
        }
        throw new UsernameNotFoundException("User Not Found");
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {

        try {
            UserDetails admin= adminDetailsService.loadUserByEmail(email);
            return admin;
        }
        catch (UsernameNotFoundException ignored) {
            // Ignore and try the next service
        }

        // Check if the user is a seller or buyer
        try {
            UserDetails seller= sellerDetailsService.loadUserByEmail(email);
            return seller;
        }
        catch (UsernameNotFoundException ignored) {
            // Ignore and try the next service
        }

        // Check if the user is a seller or buyer
        try {
            UserDetails buyer= buyerDetailsService.loadUserByEmail(email);
            return buyer;
        }
        catch (UsernameNotFoundException ignored) {
            throw new UsernameNotFoundException("User Not Found");
        }

    }
}
