package io.oxalate.fillstation.repository;

import io.oxalate.fillstation.entity.Role;
import io.oxalate.fillstation.entity.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleType roleName);
}
