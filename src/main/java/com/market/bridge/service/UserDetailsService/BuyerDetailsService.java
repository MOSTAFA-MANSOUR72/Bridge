package com.market.bridge.service.UserDetailsService;

import com.market.bridge.entity.users.UserEntity;
import com.market.bridge.repository.BuyerRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BuyerDetailsService implements UserDetailsService {
    private final BuyerRepo buyerRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return buyerRepo.findByUsername(username)
                .map(UserEntity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return buyerRepo.findBuyerByEmail(email)
                .map(UserEntity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
