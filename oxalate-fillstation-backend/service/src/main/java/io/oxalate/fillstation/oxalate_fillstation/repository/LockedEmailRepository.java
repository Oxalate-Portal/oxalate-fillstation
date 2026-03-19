package io.oxalate.fillstation.oxalate_fillstation.repository;

import io.oxalate.fillstation.oxalate_fillstation.entity.LockedEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockedEmailRepository extends JpaRepository<LockedEmail, Long> {

    Optional<LockedEmail> findByEmail(String email);

    boolean existsByEmail(String email);
}
