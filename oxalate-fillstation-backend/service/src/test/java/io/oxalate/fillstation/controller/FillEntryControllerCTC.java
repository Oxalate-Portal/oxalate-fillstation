package io.oxalate.fillstation.controller;

import io.oxalate.fillstation.api.response.FillEntryResponse;
import io.oxalate.fillstation.api.response.UserResponse;
import io.oxalate.fillstation.service.FillEntryService;
import io.oxalate.fillstation.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

class FillEntryControllerCTC {

    private MockMvc mockMvc;

    @Mock
    private FillEntryService fillEntryService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new FillEntryController(fillEntryService, userService))
                                 .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                                     @Override
                                     public boolean supportsParameter(MethodParameter parameter) {
                                         return parameter.getParameterType()
                                                         .equals(
                                                                 org.springframework.security.core.userdetails.UserDetails.class);
                                     }

                                     @Override
                                     public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                             NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                                         return new User("user@example.com", "x", List.of());
                                     }
                                 })
                                 .build();
    }

    @Test
    void getFills_authenticatedUser_returnsOnlyOwnFills() throws Exception {
        when(userService.getUserByEmail("user@example.com")).thenReturn(
                UserResponse.builder()
                            .id(8L)
                            .email("user@example.com")
                            .build());
        when(fillEntryService.getFills(8L)).thenReturn(List.of(
                FillEntryResponse.builder()
                                 .id(3L)
                                 .userId(8L)
                                 .build()));

        mockMvc.perform(get("/api/fills"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].userId").value(8));

        verify(fillEntryService).getFills(8L);
    }

    @Test
    void deleteFill_authenticatedUser_delegatesWithUserId() throws Exception {
        when(userService.getUserByEmail("user@example.com")).thenReturn(
                UserResponse.builder()
                            .id(8L)
                            .email("user@example.com")
                            .build());

        mockMvc.perform(delete("/api/fills/3"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Fill entry deleted."));

        verify(fillEntryService).delete(8L, 3L);
    }
}
