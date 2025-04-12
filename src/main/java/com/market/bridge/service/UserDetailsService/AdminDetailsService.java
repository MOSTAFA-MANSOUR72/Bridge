package com.market.bridge.service.UserDetailsService;

import com.market.bridge.repository.AdminRepo;
import com.market.bridge.entity.users.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AdminDetailsService implements UserDetailsService {
     private final AdminRepo adminRepo;
     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         return adminRepo.findByUsername(username)
                 .map(UserEntity::new)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
     }
}
