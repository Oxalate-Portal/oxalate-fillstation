package io.oxalate.fillstation.repository;

import io.oxalate.fillstation.entity.LoginHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    List<LoginHistory> findByUserIdOrderByLoginTimeDesc(Long userId);
}
