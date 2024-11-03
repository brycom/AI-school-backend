package com.example.AI_school_backend;

import com.example.Models.Dtos.RegisterUserDto;
import com.example.Models.User;
import com.example.Repositorys.UserRepository;
import com.example.Services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testSignup() {
        RegisterUserDto registerUserDto = new RegisterUserDto(null, null, null, null);
        registerUserDto.setName("Test Name");
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setUsername("testuser");
        registerUserDto.setPassword("password");

        User user = new User();
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setSubscription("free");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = authenticationService.signup(registerUserDto);

        assertEquals("Test Name", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertEquals("free", createdUser.getSubscription());
    }
}
