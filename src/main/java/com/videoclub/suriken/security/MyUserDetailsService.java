package com.videoclub.suriken.security;

import com.videoclub.suriken.security.model.AppUser;
import com.videoclub.suriken.security.model.MyUserDetail;
import com.videoclub.suriken.security.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final AppUserRepository appUserRepository;

    public MyUserDetailsService(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        AppUser user = appUserRepository.findAppUserByUserName(userName).orElseThrow();

//        return new User("marenac4", passwordEncoder.encode("cetvorka4"), new ArrayList<>());

        MyUserDetail myUserDetail = new MyUserDetail(user);
        return myUserDetail;
    }
}
