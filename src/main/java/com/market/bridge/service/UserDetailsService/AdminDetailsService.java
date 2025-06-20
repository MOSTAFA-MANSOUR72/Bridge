package com.market.bridge.service.UserDetailsService;

import com.market.bridge.entity.users.Admin;
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

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return adminRepo.findAdminByEmail(email)
                .map(UserEntity::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Long loadUserIdByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return admin.getId();
    }
}
