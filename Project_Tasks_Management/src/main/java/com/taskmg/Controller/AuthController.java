package com.taskmg.Controller;


import com.taskmg.dto.AuthRequest;
import com.taskmg.dto.AuthResponse;
import com.taskmg.model.User;
import com.taskmg.repository.UserRepository;
import com.taskmg.Security.JwtUtil; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "*") 
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
			PasswordEncoder passwordEncoder, JwtUtil jwtUtils) {
		super();
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
	}

	@PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword())); 
        
        userRepository.save(user);
        
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    	 // ADD THIS LINE FOR DEBUGGING
        System.out.println("Login attempt for email: " + request.getEmail()); 
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        
        // Use the UserDetails structure required by JwtUtils
        var userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(), 
                Collections.emptyList()
        );

        var jwtToken = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken, user.getEmail()));
    }
}