package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.Controllers.StripePaymentController;
import com.example.Models.User;
import com.example.Repositorys.UserRepository;
import com.example.Services.StripePaymentService;
import com.example.Services.UserService;

public class StripePaymentControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private StripePaymentService paymentService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private StripePaymentController stripePaymentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testCreateCustomerNewCustomer() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUsername(username)).thenReturn(user);
        when(paymentService.CreateCustomer(user)).thenReturn("newCustomerId");

        String result = stripePaymentController.createCustomer();

        assertEquals("User created successfully", result);
        assertEquals("newCustomerId", user.getCustumerNr());
    }

    @Test
    public void testCreateCustomerExistingUser() {

        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setSubscription("existingCustomerId");

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUsername(username)).thenReturn(user);

        String result = stripePaymentController.createCustomer();

        assertEquals("User created successfully", result);
        assertEquals("existingCustomerId", user.getSubscription());

    }

    @Test
    public void testCreateCustomerUserNotFound() {
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUsername(username)).thenReturn(null);

        String result = stripePaymentController.createCustomer();

        assertEquals("User not found", result);
        verify(userRepository, never()).save(any());
        verify(paymentService, never()).CreateCustomer(any());
    }

}
