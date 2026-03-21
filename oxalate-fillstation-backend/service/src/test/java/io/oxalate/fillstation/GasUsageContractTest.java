package io.oxalate.fillstation;

import io.oxalate.fillstation.entity.RoleType;
import io.oxalate.fillstation.entity.User;
import io.oxalate.fillstation.entity.UserStatus;
import io.oxalate.fillstation.repository.RoleRepository;
import io.oxalate.fillstation.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Set;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class GasUsageContractTest {

    private static final String TEST_EMAIL = "ctc-test-user@example.com";
    private static final String TEST_PASSWORD = "ctc_test_password_123";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        userRepository.findByEmail(TEST_EMAIL).ifPresent(userRepository::delete);

        var userRole = roleRepository.findByRoleName(RoleType.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in test DB"));

        User user = User.builder()
                .name("CTC Test User")
                .email(TEST_EMAIL)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .status(UserStatus.ACTIVE)
                .roles(Set.of(userRole))
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.findByEmail(TEST_EMAIL).ifPresent(userRepository::delete);
    }

    @Test
    void loginAndGetMyGasUsage_shouldSucceed() throws Exception {
        String loginBody = """
                {"email":"%s","password":"%s"}
                """.formatted(TEST_EMAIL, TEST_PASSWORD);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("auth_token"))
                .andReturn();

        Cookie jwtCookie = loginResult.getResponse().getCookie("auth_token");

        mockMvc.perform(get("/api/users/me/gas-usage")
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalO2Added").exists())
                .andExpect(jsonPath("$.totalHeAdded").exists())
                .andExpect(jsonPath("$.totalGasAdded").exists())
                .andExpect(jsonPath("$.sinceLastZeroO2Added").exists())
                .andExpect(jsonPath("$.sinceLastZeroHeAdded").exists())
                .andExpect(jsonPath("$.sinceLastZeroGasAdded").exists());
    }

    @Test
    void getMyGasUsage_withoutAuthentication_shouldReturn401() throws Exception {
        mockMvc.perform(get("/api/users/me/gas-usage"))
                .andExpect(status().isUnauthorized());
    }
}
