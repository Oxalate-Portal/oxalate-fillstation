package io.oxalate.fillstation.oxalate_fillstation.repository;

import io.oxalate.fillstation.oxalate_fillstation.entity.User;
import io.oxalate.fillstation.oxalate_fillstation.entity.UserStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByStatus(UserStatus status);

    boolean existsByEmail(String email);
}
