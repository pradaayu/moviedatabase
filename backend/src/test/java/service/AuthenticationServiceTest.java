package service;

import model.User;
import model.UserLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import repository.UserLoginRepository;
import repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User testUser;
    private UserLogin testUserLogin;

    @BeforeEach
    void setUp() {
        // Set up mock user and user login objects
        testUser = new User();
        testUser.setId("u-123ABC");
        testUser.setName("John Doe");

        testUserLogin = new UserLogin();
        testUserLogin.setEmail("test@example.com");
        testUserLogin.setPassword("encodedPassword"); // Mocked encoded password
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Mock behavior
        when(userLoginRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Call the method
        authenticationService.createUser(testUser, "rawPassword", "test@example.com");

        // Verify interactions
        verify(userLoginRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionIfEmailAlreadyRegistered() {
        // Mock behavior
        when(userLoginRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUserLogin));

        // Call the method and expect an exception
        assertThrows(IllegalArgumentException.class, () ->
                authenticationService.createUser(testUser, "rawPassword", "test@example.com"));

        // Verify interactions
        verify(userLoginRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(0)).encode(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void shouldValidateLoginSuccessfully() {
        // Mock behavior
        when(userLoginRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUserLogin));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        // Call the method
        boolean isValid = authenticationService.validateLogin("test@example.com", "rawPassword");

        // Assert and verify
        assertThat(isValid).isTrue();
        verify(userLoginRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("rawPassword", "encodedPassword");
    }

    @Test
    void shouldReturnFalseForInvalidLogin() {
        // Mock behavior
        when(userLoginRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Call the method
        boolean isValid = authenticationService.validateLogin("test@example.com", "rawPassword");

        // Assert and verify
        assertThat(isValid).isFalse();
        verify(userLoginRepository, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
    }
}