package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.request.LoginRequest;
import io.oxalate.fillstation.api.response.LoginResponse;
import io.oxalate.fillstation.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthControllerCTC {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setup() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService))
                                 .build();
    }

    @Test
    void login_validCredentials_ReturnsUserAndDelegates() throws Exception {
        when(authService.login(
                org.mockito.ArgumentMatchers.any(LoginRequest.class),
                org.mockito.ArgumentMatchers.any(HttpServletRequest.class),
                org.mockito.ArgumentMatchers.any(HttpServletResponse.class)))
                .thenReturn(LoginResponse.builder()
                                         .userId(4L)
                                         .email("user@example.com")
                                         .name("User")
                                         .roles(java.util.List.of("ROLE_USER"))
                                         .language("fi")
                                         .build());

        mockMvc.perform(post("/api/auth/login")
                       .contentType("application/json")
                       .content("""
                               {"email":"user@example.com","password":"password"}
                               """))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.userId").value(4))
               .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));

        verify(authService).login(
                org.mockito.ArgumentMatchers.any(LoginRequest.class),
                org.mockito.ArgumentMatchers.any(HttpServletRequest.class),
                org.mockito.ArgumentMatchers.any(HttpServletResponse.class));
    }

    @Test
    void logout_returnsSuccessMessage() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Logged out successfully."));
        verify(authService).logout(org.mockito.ArgumentMatchers.any(HttpServletResponse.class));
    }

    @Test
    void register_invalidJson_isRejected() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                       .contentType("application/json")
                       .content("""
                               {"name":"","email":"not-an-email","password":"short","language":""}
                               """))
               .andExpect(status().isBadRequest());
    }
}
