package io.oxalate.fillstation.oxalate_fillstation.service;

import io.oxalate.fillstation.oxalate_fillstation.entity.Role;
import io.oxalate.fillstation.oxalate_fillstation.entity.RoleType;
import io.oxalate.fillstation.oxalate_fillstation.entity.User;
import io.oxalate.fillstation.oxalate_fillstation.entity.UserStatus;
import io.oxalate.fillstation.oxalate_fillstation.repository.RoleRepository;
import io.oxalate.fillstation.oxalate_fillstation.repository.UserRepository;
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

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user '{}' already exists; skipping creation.", adminEmail);
            return;
        }

        User admin = User.builder()
                .name("Administrator")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .language("en")
                .status(UserStatus.ACTIVE)
                .roles(new HashSet<>(Set.of(adminRole)))
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(admin);
        log.info("Initial admin user '{}' created successfully.", adminEmail);
    }
}
