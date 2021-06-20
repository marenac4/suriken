package com.videoclub.suriken.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyUserDetail implements UserDetails {

    private final String userName;
    private final String password;
    private final boolean active;
    private final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

    public MyUserDetail(AppUser appUser) {
        this.userName = appUser.getUserName();
        this.password = appUser.getPassword();
        this.active = true;

        appUser.getRoles().forEach(role -> {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            role.getPrivileges().forEach(privilege -> this.authorities.add(new SimpleGrantedAuthority(privilege.getName())));
        });

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
