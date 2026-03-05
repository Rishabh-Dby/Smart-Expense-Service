package com.rishabh.smartexpensetracker.service;

import com.rishabh.smartexpensetracker.dto.AuthResponse;
import com.rishabh.smartexpensetracker.dto.LoginRequest;
import com.rishabh.smartexpensetracker.dto.RegisterRequest;
import com.rishabh.smartexpensetracker.entity.User;
import com.rishabh.smartexpensetracker.repository.UserRepository;
import com.rishabh.smartexpensetracker.utility.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_throwsWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@test.com");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(new User()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(request));
        assertEquals("Email already registered", ex.getMessage());
    }

    @Test
    void register_savesUserWithEncodedPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setName("User");
        request.setEmail("user@test.com");
        request.setPassword("raw-password");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");

        authService.register(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("User", saved.getName());
        assertEquals("user@test.com", saved.getEmail());
        assertEquals("encoded-password", saved.getPassword());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void login_authenticatesAndReturnsJwt() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@test.com");
        request.setPassword("secret");

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("user@test.com", "secret"));
        when(jwtUtil.generateToken("user@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertEquals("jwt-token", response.getToken());
    }
}
