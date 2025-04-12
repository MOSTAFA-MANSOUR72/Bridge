package com.market.bridge.entity.users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserEntity implements UserDetails {
    private final String roles;
    private final String password;
    private final String username;

    public UserEntity(Buyer buyer){
        roles = buyer.getRoles();
        password = buyer.getPassword();
        username = buyer.getUsername();
    }

    public UserEntity(Seller seller){
        roles = seller.getRoles();
        password = seller.getPassword();
        username = seller.getUsername();
    }

    public UserEntity(Admin admin){
        roles = admin.getRoles();
        password = admin.getPassword();
        username = admin.getUsername();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String > rolesList = Arrays.stream(roles.split(",")).toList();
        return rolesList.stream().
                map(u -> (GrantedAuthority) () -> "ROLE_"+u)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }


//    // commons
//    //**
//    @Column(name = "Company_name")
//    private String companyName;
//
//    @ManyToMany
//    @JoinTable(
//            name = "business_type",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "category_id")
//    )
//    List<Category> businessTypes; // tags buyer browse , tags seller sells
//    //
//    //**


}
