package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.response.ConfigurationResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.AuthService;
import io.oxalate.fillstation.service.ConfigurationService;
import io.oxalate.fillstation.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AdminControllerCTC {

    private MockMvc mockMvc;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setup() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(configurationService, userService, authService))
                                 .build();
    }

    @Test
    void getUsers_usersExist_Ok() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(UserResponse.builder()
                                                                       .id(1L)
                                                                       .name("Admin User")
                                                                       .email("user@example.com")
                                                                       .language("en")
                                                                       .status("ACTIVE")
                                                                       .roles(List.of("ROLE_USER"))
                                                                       .createdAt(LocalDateTime.now())
                                                                       .build()));

        mockMvc.perform(get("/api/admin/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].email").value("user@example.com"));
    }

    @Test
    void activateUser_existingId_Ok() throws Exception {
        when(userService.activateUserAccount(4L)).thenReturn(UserResponse.builder()
                                                                         .id(4L)
                                                                         .name("Activated")
                                                                         .email("activated@example.com")
                                                                         .language("en")
                                                                         .status("ACTIVE")
                                                                         .roles(List.of("ROLE_USER"))
                                                                         .createdAt(LocalDateTime.now())
                                                                         .build());

        mockMvc.perform(post("/api/admin/users/4/activate")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void sendPasswordReset_existingId_Ok() throws Exception {
        mockMvc.perform(post("/api/admin/users/5/password-reset")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Password reset email sent."));

        verify(authService).requestPasswordResetForUser(5L);
    }

    @Test
    void closeUser_existingId_Ok() throws Exception {
        when(userService.closeUserAccount(7L)).thenReturn(UserResponse.builder()
                                                                      .id(7L)
                                                                      .name("Closed")
                                                                      .email("closed@example.com")
                                                                      .language("en")
                                                                      .status("CLOSED")
                                                                      .roles(List.of("ROLE_USER"))
                                                                      .createdAt(LocalDateTime.now())
                                                                      .build());

        mockMvc.perform(post("/api/admin/users/7/close")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void anonymizeUser_existingId_Ok() throws Exception {
        mockMvc.perform(post("/api/admin/users/9/anonymize")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Account anonymized successfully."));

        verify(userService).anonymizeUserByAdmin(anyLong());
    }

    @Test
    void getConfig_entriesExist_Ok() throws Exception {
        when(configurationService.getAll()).thenReturn(List.of(ConfigurationResponse.builder()
                                                                                    .id(1L)
                                                                                    .groupName("mail")
                                                                                    .configKey("from")
                                                                                    .configValue("noreply@example.com")
                                                                                    .build()));

        mockMvc.perform(get("/api/admin/config"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].groupName").value("mail"));
    }
}

