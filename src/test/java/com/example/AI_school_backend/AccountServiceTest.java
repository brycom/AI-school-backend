package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.Models.User;
import com.example.Repositorys.UserRepository;
import com.example.Services.AccountService;

public class AccountServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    private User newUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        newUser = new User("newUserName", "newUserPassword", "newUserEmail", "newUser", null);
    }

    @Test
    void testCreateAccount() {
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(null);
        when(userRepository.save(newUser)).thenReturn(newUser);

        User savedUser = accountService.createAccount(newUser);

        assertEquals(newUser.getUsername(), savedUser.getUsername());
        assertEquals(newUser.getEmail(), savedUser.getEmail());

    }

    @Test
    void testCreateAccount_ThrowsException_WhenUsernameExists() {
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(newUser));

        assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(newUser);
        });
    }

}
