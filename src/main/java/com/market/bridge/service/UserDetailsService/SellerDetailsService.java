package com.market.bridge.service.UserDetailsService;

import com.market.bridge.repository.SellerRepo;
import com.market.bridge.entity.users.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SellerDetailsService implements UserDetailsService {
    private final SellerRepo sellerRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return sellerRepo.findByUsername(username)
                .map(UserEntity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
