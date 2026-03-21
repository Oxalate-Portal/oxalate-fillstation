package io.oxalate.fillstation.service;

import io.oxalate.fillstation.entity.Role;
import io.oxalate.fillstation.entity.RoleType;
import io.oxalate.fillstation.entity.User;
import io.oxalate.fillstation.entity.UserStatus;
import io.oxalate.fillstation.repository.RoleRepository;
import io.oxalate.fillstation.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class InitialAdminServiceUTC {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private InitialAdminService initialAdminService;

    @Test
    void createInitialAdminIfNeeded_whenCredentialsMissing_skipCreation_Ok() {
        initialAdminService.createInitialAdminIfNeeded("", "");

        verify(roleRepository, never()).findByRoleName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void createInitialAdminIfNeeded_whenUserDoesNotExist_createAdmin_Ok() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleType.ROLE_ADMIN);

        when(roleRepository.findByRoleName(RoleType.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-password");

        initialAdminService.createInitialAdminIfNeeded("admin@example.com", "new-password");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User createdUser = userCaptor.getValue();
        assertEquals("admin@example.com", createdUser.getEmail());
        assertEquals(UserStatus.ACTIVE, createdUser.getStatus());
        assertTrue(createdUser.getRoles()
                              .contains(adminRole));
        assertEquals("encoded-password", createdUser.getPassword());
    }

    @Test
    void createInitialAdminIfNeeded_whenUserExists_updatePasswordAndRole_Ok() {
        Role adminRole = new Role();
        adminRole.setRoleName(RoleType.ROLE_ADMIN);

        User existingUser = User.builder()
                                .name("Existing Admin")
                                .email("admin@example.com")
                                .password("old-password")
                                .status(UserStatus.PENDING)
                                .roles(new HashSet<>(Set.of()))
                                .build();

        when(roleRepository.findByRoleName(RoleType.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("new-password")).thenReturn("encoded-password");

        initialAdminService.createInitialAdminIfNeeded("admin@example.com", "new-password");

        verify(userRepository).save(existingUser);
        assertEquals(UserStatus.ACTIVE, existingUser.getStatus());
        assertTrue(existingUser.getRoles()
                               .contains(adminRole));
        assertEquals("encoded-password", existingUser.getPassword());
    }
}

