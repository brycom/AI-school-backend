package com.example.AI_school_backend;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.Repositorys.UserRepository;
import com.example.Services.UserService;

public class UserSeviceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserIdFromUsernameUserDoesNotExist() {

        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        UUID result = userService.getUserIdFromUsername(username);

        assertNull(result, "Expected null to be returned when user does not exist");
    }

}
