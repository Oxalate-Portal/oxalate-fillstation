package io.oxalate.fillstation.service;

import io.oxalate.fillstation.api.request.UserUpdateRequest;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.entity.LockedEmail;
import io.oxalate.fillstation.entity.User;
import io.oxalate.fillstation.entity.UserStatus;
import io.oxalate.fillstation.repository.LockedEmailRepository;
import io.oxalate.fillstation.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LockedEmailRepository lockedEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserResponse getUser(Long userId) {
        User user = findUser(userId);
        return toResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toResponse(user);
    }

    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = findUser(userId);
        user.setName(request.getName());
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
            user.setEmail(request.getEmail());
        }
        user.setLanguage(request.getLanguage());
        user.setUpdatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<UserResponse> getPendingRegistrations() {
        return userRepository.findByStatusAndEmailVerifiedTrue(UserStatus.PENDING)
                             .stream()
                             .map(this::toResponse)
                             .toList();
    }

    public UserResponse updateUserStatus(Long userId, String statusStr) {
        User user = findUser(userId);
        UserStatus newStatus;
        try {
            newStatus = UserStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user status");
        }
        UserStatus oldStatus = user.getStatus();
        user.setStatus(newStatus);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        if (oldStatus == UserStatus.PENDING && newStatus == UserStatus.ACTIVE) {
            emailService.sendAccountApprovedEmail(user.getEmail(), user.getName(), user.getLanguage());
        }
        return toResponse(user);
    }

    @Transactional
    public UserResponse approvePendingRegistration(Long userId) {
        User user = findUser(userId);
        if (!user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User email is not verified");
        }
        return updateUserStatus(userId, UserStatus.ACTIVE.name());
    }

    @Transactional
    public UserResponse rejectPendingRegistration(Long userId) {
        User user = findUser(userId);
        if (!lockedEmailRepository.existsByEmail(user.getEmail())) {
            lockedEmailRepository.save(LockedEmail.builder()
                                                  .email(user.getEmail())
                                                  .build());
        }
        return updateUserStatus(userId, UserStatus.LOCKED.name());
    }

    /**
     * Activates a user account so credentials can be used for login.
     *
     * @param userId user id to activate
     * @return updated user details
     */
    @Transactional
    public UserResponse activateUserAccount(Long userId) {
        return updateUserStatus(userId, UserStatus.ACTIVE.name());
    }

    /**
     * Closes a user account while preserving personal data.
     *
     * @param userId user id to close
     * @return updated user details
     */
    @Transactional
    public UserResponse closeUserAccount(Long userId) {
        return updateUserStatus(userId, UserStatus.CLOSED.name());
    }

    @Transactional
    public void anonymizeUser(Long userId) {
        User user = findUser(userId);
        String anonymizedEmail = "deleted_" + UUID.randomUUID() + "@deleted.invalid";
        lockedEmailRepository.save(LockedEmail.builder().email(user.getEmail()).build());
        user.setEmail(anonymizedEmail);
        user.setName("Deleted User");
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setStatus(UserStatus.LOCKED);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * Anonymizes a user account through an administrator action.
     *
     * @param userId user id to anonymize
     */
    @Transactional
    public void anonymizeUserByAdmin(Long userId) {
        anonymizeUser(userId);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .language(user.getLanguage())
                .status(user.getStatus().name())
                .roles(user.getRoles().stream().map(r -> r.getRoleName().name()).toList())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
