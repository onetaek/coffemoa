package com.coffemoa.domain.auth.entity;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "auth_role",
    uniqueConstraints = @UniqueConstraint(name = "uk_role_name", columnNames = {"role_name"})
)
@SQLDelete(sql = "UPDATE auth_role SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Role extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role_code", length = 50, nullable = false) // 내부 식별자
  private String code;

  @Column(name = "role_name", length = 100, nullable = false) // 표시명
  private String name;

  /**
   * 0/1
   */
  @Column(name = "status", nullable = false)
  private Integer status = 1;

  @Column(name = "remark", length = 500)
  private String remark;
}