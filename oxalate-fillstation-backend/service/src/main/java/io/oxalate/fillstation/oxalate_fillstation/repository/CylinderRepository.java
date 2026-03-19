package io.oxalate.fillstation.oxalate_fillstation.repository;

import io.oxalate.fillstation.oxalate_fillstation.entity.Cylinder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CylinderRepository extends JpaRepository<Cylinder, Long> {

    List<Cylinder> findByUserId(Long userId);

    Optional<Cylinder> findByIdAndUserId(Long id, Long userId);
}
