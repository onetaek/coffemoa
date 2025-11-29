package com.coffemoa.domain.standard.unit;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "unit",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_unit_name",
            columnNames = {"name", "deleted_at"}
        )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Unit extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 단위명 (예: g, ml, 개)
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * 단위 설명
   */
  @Column(name = "description")
  private String description;
}
