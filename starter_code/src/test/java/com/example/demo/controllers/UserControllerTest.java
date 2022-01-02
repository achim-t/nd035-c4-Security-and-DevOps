package com.example.demo.controllers;


import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    public static final String TEST_USER = "test";
    public static final String TEST_PASSWORD = "testPassword";
    public static final String THIS_IS_HASHED = "thisIsHashed";
    @InjectMocks
    private UserController userController;

    @Mock
    private final UserRepository userRepo = mock(UserRepository.class);

    @Mock
    private final CartRepository cartRepo = mock(CartRepository.class);

    @Mock
    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUserHappyPath() {
        when(encoder.encode(TEST_PASSWORD)).thenReturn(THIS_IS_HASHED);
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(TEST_USER);
        request.setPassword(TEST_PASSWORD);
        request.setConfirmPassword(TEST_PASSWORD);
        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(TEST_USER, user.getUsername());
        assertEquals(THIS_IS_HASHED, user.getPassword());
    }
    @Test
    public void createUserFail() {
        when(encoder.encode(TEST_PASSWORD)).thenReturn(THIS_IS_HASHED);
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(TEST_USER);
        request.setPassword(TEST_PASSWORD);
        request.setConfirmPassword("different");
        final ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
    @Test
    public void findByIdFail() {
        createUser();

        when(userRepo.findById(0L)).thenReturn(java.util.Optional.empty());

        final ResponseEntity<User> userResponseEntity = userController.findById(0L);
        assertNotNull(userResponseEntity);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
    }

    private User createUser() {
        when(encoder.encode(TEST_PASSWORD)).thenReturn(THIS_IS_HASHED);
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(TEST_USER);
        request.setPassword(TEST_PASSWORD);
        request.setConfirmPassword(TEST_PASSWORD);
        final ResponseEntity<User> response = userController.createUser(request);
        return response.getBody();
    }

    @Test
    public void findById() {
        User user = createUser();

        when(userRepo.findById(0L)).thenReturn(java.util.Optional.ofNullable(user));

        final ResponseEntity<User> userResponseEntity = userController.findById(0L);

        user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(TEST_USER, user.getUsername());
        assertEquals(THIS_IS_HASHED, user.getPassword());
    }

    @Test
    public void findByUserName() {
        User user = createUser();
        when(userRepo.findByUsername(TEST_USER)).thenReturn(user);

        final ResponseEntity<User> userResponseEntity = userController.findByUserName(TEST_USER);

        user = userResponseEntity.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(TEST_USER, user.getUsername());
        assertEquals(THIS_IS_HASHED, user.getPassword());
    }
    @Test
    public void findByUserNameFail() {

        final ResponseEntity<User> userResponseEntity = userController.findByUserName(TEST_USER);

        assertNotNull(userResponseEntity);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
    }
}