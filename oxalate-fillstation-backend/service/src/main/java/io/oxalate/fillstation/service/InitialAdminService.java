package io.oxalate.fillstation.service;

import io.oxalate.fillstation.entity.Role;
import io.oxalate.fillstation.entity.RoleType;
import io.oxalate.fillstation.entity.User;
import io.oxalate.fillstation.entity.UserStatus;
import io.oxalate.fillstation.repository.RoleRepository;
import io.oxalate.fillstation.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialAdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createInitialAdminIfNeeded(String adminEmail, String adminPassword) {
        if (adminEmail == null || adminEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
            log.info("INITIAL_ADMIN_EMAIL or INITIAL_ADMIN_PASSWORD not set; skipping initial admin creation.");
            return;
        }

        Role adminRole = roleRepository.findByRoleName(RoleType.ROLE_ADMIN)
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found in database"));

        userRepository.findByEmail(adminEmail)
                      .ifPresentOrElse(existingAdmin -> {
                          Set<Role> roles = existingAdmin.getRoles() == null ? new HashSet<>() : existingAdmin.getRoles();
                          roles.add(adminRole);

                          existingAdmin.setRoles(roles);
                          existingAdmin.setStatus(UserStatus.ACTIVE);
                          existingAdmin.setEmailVerified(true);
                          existingAdmin.setPassword(passwordEncoder.encode(adminPassword));
                          existingAdmin.setUpdatedAt(LocalDateTime.now());

                          userRepository.save(existingAdmin);
                          log.info("Admin user '{}' already existed; password and admin role were updated.", adminEmail);
                      }, () -> {
                          User admin = User.builder()
                                           .name("Administrator")
                                           .email(adminEmail)
                                           .password(passwordEncoder.encode(adminPassword))
                                           .language("en")
                                           .status(UserStatus.ACTIVE)
                                           .emailVerified(true)
                                           .roles(new HashSet<>(Set.of(adminRole)))
                                           .updatedAt(LocalDateTime.now())
                                           .build();

                          userRepository.save(admin);
                          log.info("Initial admin user '{}' created successfully.", adminEmail);
                      });
    }
}
