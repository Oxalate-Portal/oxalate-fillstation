package io.oxalate.fillstation.oxalate_fillstation.service;

import io.oxalate.fillstation.oxalate_fillstation.entity.LoginHistory;
import io.oxalate.fillstation.oxalate_fillstation.repository.LoginHistoryRepository;
import io.oxalate.fillstation.api.response.LoginHistoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    public void record(Long userId, String ipAddress) {
        LoginHistory entry = LoginHistory.builder()
                .userId(userId)
                .ipAddress(ipAddress)
                .build();
        loginHistoryRepository.save(entry);
    }

    public List<LoginHistoryResponse> getHistory(Long userId) {
        return loginHistoryRepository.findByUserIdOrderByLoginTimeDesc(userId).stream()
                .map(h -> LoginHistoryResponse.builder()
                        .id(h.getId())
                        .loginTime(h.getLoginTime())
                        .ipAddress(h.getIpAddress())
                        .build())
                .toList();
    }
}
