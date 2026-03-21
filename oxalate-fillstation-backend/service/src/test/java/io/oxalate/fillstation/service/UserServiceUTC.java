package io.oxalate.fillstation.service;

import io.oxalate.fillstation.entity.User;
import io.oxalate.fillstation.entity.UserStatus;
import io.oxalate.fillstation.repository.LockedEmailRepository;
import io.oxalate.fillstation.repository.UserRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UserServiceUTC {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LockedEmailRepository lockedEmailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void approvePendingRegistration_whenEmailNotVerified_throwBadRequest_Fail() {
        User user = User.builder()
                        .id(1L)
                        .email("pending@example.com")
                        .name("Pending")
                        .status(UserStatus.PENDING)
                        .emailVerified(false)
                        .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.approvePendingRegistration(1L));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void rejectPendingRegistration_whenEmailNotLocked_storeLockedEmail_Ok() {
        User user = User.builder()
                        .id(2L)
                        .email("to.reject@example.com")
                        .name("Reject Me")
                        .status(UserStatus.PENDING)
                        .emailVerified(true)
                        .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(lockedEmailRepository.existsByEmail("to.reject@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.rejectPendingRegistration(2L);

        verify(lockedEmailRepository).save(any());
        verify(userRepository).save(user);
    }
}

