package com.hisham.HomeCentre.controllers;

import com.hisham.HomeCentre.exceptions.CustomExceptions.ConflictException;
import com.hisham.HomeCentre.models.Role;
import com.hisham.HomeCentre.models.RoleEnum;
import com.hisham.HomeCentre.models.User;
import com.hisham.HomeCentre.payloads.APIResponse;
import com.hisham.HomeCentre.payloads.authentication.SignIn.JWTAuthenticationResponse;
import com.hisham.HomeCentre.payloads.authentication.SignIn.SignInRequest;
import com.hisham.HomeCentre.payloads.authentication.SignUp.SignUpRequest;
import com.hisham.HomeCentre.repositories.RoleRepository;
import com.hisham.HomeCentre.repositories.UserRepository;
import com.hisham.HomeCentre.utils.JWTUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity<JWTAuthenticationResponse> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmailOrUsername(),
                        signInRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new JWTAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ConflictException("Username already in use!");
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ConflictException("Email already in use!");
        }

        // Creating user's account
        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getPhone(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User Role not set."));

        user.getRoles().add(userRole);

        User result = userRepository.save(user);


        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new APIResponse(true, "User registered successfully"));
    }
}
