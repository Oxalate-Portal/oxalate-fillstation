package io.oxalate.fillstation.repository;

import io.oxalate.fillstation.entity.EmailToken;
import io.oxalate.fillstation.entity.TokenType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findByTokenAndType(String token, TokenType type);

    List<EmailToken> findByUserIdAndType(Long userId, TokenType type);

    @Modifying
    @Query("DELETE FROM EmailToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
