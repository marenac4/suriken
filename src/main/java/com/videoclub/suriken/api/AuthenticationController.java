package com.videoclub.suriken.api;

import com.videoclub.suriken.security.MyUserDetailsService;
import com.videoclub.suriken.jwt.AuthenticationRequest;
import com.videoclub.suriken.jwt.AuthenticationResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/authenticate")
@RestController
public class AuthenticationController {

    @Value("${SURIKEN_SECRET_KEY}")
    String key;

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationManager authenticationManager;

    private MyUserDetailsService userDetailsService;

    public AuthenticationController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            Authentication authenticate = new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword());
            Authentication authenticationResult = authenticationManager.authenticate(authenticate);
            logger.debug(String.valueOf(authenticationResult.isAuthenticated()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username and password", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());

        Map<String, Object> claims = new HashMap<>();


        String jwt = Jwts.builder()
                .claim("authorities", userDetails.getAuthorities())
                .setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(2)))
                .signWith(Keys.hmacShaKeyFor(key.getBytes())).compact();

        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);

    }
}
