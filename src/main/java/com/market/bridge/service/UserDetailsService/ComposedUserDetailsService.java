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
}
