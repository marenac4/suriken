package com.videoclub.suriken.init;

import com.videoclub.suriken.security.model.AppUser;
import com.videoclub.suriken.security.model.Privilege;
import com.videoclub.suriken.security.model.Role;
import com.videoclub.suriken.security.repository.AppUserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    public SetupDataLoader(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (alreadySetup) return;

        Privilege privilege1 = new Privilege("movie:write");
        Privilege privilege2 = new Privilege("movie:read");
        Privilege privilege3 = new Privilege("renter:write");
        Privilege privilege4 = new Privilege("renter:read");

        Set<Privilege> adminPrivileges = new HashSet<>(Arrays.asList(privilege1, privilege2, privilege3, privilege4));

        Role adminRole = new Role("ADMIN");
        adminRole.setPrivileges(adminPrivileges);

        AppUser adminUser = new AppUser(null, "marenac", passwordEncoder.encode("cetvorka"), new HashSet<>(Arrays.asList(adminRole)));

        appUserRepository.save(adminUser);

        Role renterRole = new Role("USER");
        renterRole.setPrivileges(new HashSet<>(Arrays.asList(privilege2, privilege4)));

        AppUser userUser = new AppUser(null, "djina", passwordEncoder.encode("grand"), new HashSet<>(Arrays.asList(renterRole)));

        appUserRepository.save(userUser);


    }

}
