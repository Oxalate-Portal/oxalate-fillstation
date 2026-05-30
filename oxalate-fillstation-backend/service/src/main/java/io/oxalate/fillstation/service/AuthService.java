package io.oxalate.fillstation.service;

import io.oxalate.fillstation.api.request.LoginRequest;
import io.oxalate.fillstation.api.request.PasswordChangeRequest;
import io.oxalate.fillstation.api.request.PasswordResetRequest;
import io.oxalate.fillstation.api.request.RegistrationRequest;
import io.oxalate.fillstation.api.response.LoginResponse;
import io.oxalate.fillstation.entity.EmailToken;
import io.oxalate.fillstation.entity.RoleType;
import io.oxalate.fillstation.entity.TokenType;
import io.oxalate.fillstation.entity.User;
import io.oxalate.fillstation.entity.UserStatus;
import io.oxalate.fillstation.repository.EmailTokenRepository;
import io.oxalate.fillstation.repository.LockedEmailRepository;
import io.oxalate.fillstation.repository.RoleRepository;
import io.oxalate.fillstation.repository.UserRepository;
import io.oxalate.fillstation.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final LockedEmailRepository lockedEmailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final LoginHistoryService loginHistoryService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public void register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        if (lockedEmailRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This email address cannot be used");
        }

        var userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found"));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .language(request.getLanguage())
                .status(UserStatus.PENDING)
                .roles(new HashSet<>(Set.of(userRole)))
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String token = generateAndSaveToken(user.getId(), TokenType.VERIFICATION, 24);
        String verificationLink = baseUrl + "/api/auth/verify-email?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationLink, user.getLanguage());
    }

    @Transactional
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getStatus() == UserStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account pending approval");
        }
        if (user.getStatus() == UserStatus.LOCKED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is locked");
        }
        if (user.getStatus() == UserStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is closed");
        }

        String jwtToken = jwtTokenProvider.generateToken(user.getEmail());
        jwtTokenProvider.addJwtCookie(httpResponse, jwtToken);

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        loginHistoryService.record(user.getId(), ipAddress);

        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(r -> r.getRoleName().name()).toList())
                .language(user.getLanguage())
                .build();
    }

    public void logout(HttpServletResponse response) {
        jwtTokenProvider.clearJwtCookie(response);
    }

    @Transactional
    public void verifyEmail(String token) {
        EmailToken emailToken = emailTokenRepository.findByTokenAndType(token, TokenType.VERIFICATION)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid verification token"));

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification token expired");
        }

        User user = userRepository.findById(emailToken.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Keep account in PENDING until operator/admin approval
        emailTokenRepository.delete(emailToken);
    }

    @Transactional
    public void requestPasswordReset(PasswordResetRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            // Delete existing reset tokens for this user
            emailTokenRepository.findByUserIdAndType(user.getId(), TokenType.PASSWORD_RESET)
                    .forEach(emailTokenRepository::delete);

            String token = generateAndSaveToken(user.getId(), TokenType.PASSWORD_RESET, 1);
            String resetLink = baseUrl + "/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), resetLink, user.getLanguage());
        });
    }

    /**
     * Sends a password reset link for an existing user account.
     *
     * @param userId the user id receiving the reset link
     */
    @Transactional
    public void requestPasswordResetForUser(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        emailTokenRepository.findByUserIdAndType(user.getId(), TokenType.PASSWORD_RESET)
                            .forEach(emailTokenRepository::delete);

        String token = generateAndSaveToken(user.getId(), TokenType.PASSWORD_RESET, 1);
        String resetLink = baseUrl + "/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), resetLink, user.getLanguage());
    }

    @Transactional
    public void resetPassword(PasswordChangeRequest request) {
        EmailToken emailToken = emailTokenRepository.findByTokenAndType(request.getToken(), TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reset token"));

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reset token expired");
        }

        User user = userRepository.findById(emailToken.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        emailTokenRepository.delete(emailToken);
    }

    private String generateAndSaveToken(Long userId, TokenType type, int expiryHours) {
        String token = UUID.randomUUID().toString();
        EmailToken emailToken = EmailToken.builder()
                .userId(userId)
                .token(token)
                .type(type)
                .expiresAt(LocalDateTime.now().plusHours(expiryHours))
                .build();
        emailTokenRepository.save(emailToken);
        return token;
    }
}
