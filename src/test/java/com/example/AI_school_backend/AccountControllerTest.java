package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.Models.MembershipTear;
import com.example.Models.User;
import com.example.Models.Dtos.UserDto;
import com.example.Repositorys.MembershipTearRepository;
import com.example.Repositorys.UserRepository;
import com.example.Controllers.AccountController;

public class AccountControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MembershipTearRepository membershipTearRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetProfileUserExistsWithSubscription() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setSubscription("price_123");

        MembershipTear membershipTear = new MembershipTear();
        membershipTear.setPriceId("price_123");
        membershipTear.setTear("Gold");

        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(membershipTearRepository.findByPriceId("price_123")).thenReturn(membershipTear);

        UserDto result = accountController.getProfile();

        assertNotNull(result, "Expected a non-null UserDto");
        assertEquals(username, result.getUsername(), "Expected username to match");
        assertEquals("test@example.com", result.getEmail(), "Expected email to match");
        assertEquals("Test User", result.getName(), "Expected name to match");
        assertEquals("Gold", result.getSubscription(), "Expected subscription to match");
    }

    @Test
    public void testGetProfileUserDoesNotExist() {
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UserDto result = accountController.getProfile();

        assertNull(result, "Expected null when user does not exist");
    }

}
