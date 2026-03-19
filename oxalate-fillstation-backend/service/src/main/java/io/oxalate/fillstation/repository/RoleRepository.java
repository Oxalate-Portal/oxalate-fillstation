package io.oxalate.fillstation.oxalate_fillstation.repository;

import io.oxalate.fillstation.oxalate_fillstation.entity.Role;
import io.oxalate.fillstation.oxalate_fillstation.entity.RoleType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleType roleName);
}
