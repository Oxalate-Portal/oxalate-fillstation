package io.oxalate.fillstation.oxalate_fillstation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cylinders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cylinder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal volume;

    @Column(name = "working_pressure", nullable = false, precision = 10, scale = 2)
    private BigDecimal workingPressure;

    @Column(name = "serial_number")
    private String serialNumber;
}
