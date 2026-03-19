package io.oxalate.fillstation.oxalate_fillstation.repository;

import io.oxalate.fillstation.oxalate_fillstation.entity.FillEntry;
import io.oxalate.fillstation.oxalate_fillstation.entity.FillStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FillEntryRepository extends JpaRepository<FillEntry, Long> {

    List<FillEntry> findByUserId(Long userId);

    List<FillEntry> findByUserIdAndStatus(Long userId, FillStatus status);

    Optional<FillEntry> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COALESCE(SUM(f.o2Added),0), COALESCE(SUM(f.heAdded),0), COALESCE(SUM(f.gasAdded),0) " +
            "FROM FillEntry f WHERE f.userId = :userId")
    Object[] sumsByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(f.o2Added),0), COALESCE(SUM(f.heAdded),0), COALESCE(SUM(f.gasAdded),0) " +
            "FROM FillEntry f WHERE f.userId = :userId AND f.status != 'ZEROED'")
    Object[] sumsSinceLastZero(@Param("userId") Long userId);

    List<FillEntry> findByStatus(FillStatus status);
}
