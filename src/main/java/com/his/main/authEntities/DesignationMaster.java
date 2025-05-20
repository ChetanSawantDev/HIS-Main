package com.his.main.authEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HIS_DESIGNATION_MASTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignationMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String designationId;

    @Column(length = 50, unique = true, nullable = false)
    private String designationName;

    @Column(nullable = false)
    private int priority; // 5 (lowest) to 10 (highest)

}
