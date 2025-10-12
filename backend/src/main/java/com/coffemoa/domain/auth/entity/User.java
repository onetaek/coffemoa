package com.coffemoa.domain.auth.entity;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "auth_user",
    indexes = {
        @Index(name = "ix_user_account", columnList = "account"),
        @Index(name = "ix_user_dept", columnList = "department_id")
    },
    uniqueConstraints = @UniqueConstraint(name = "uk_user_account", columnNames = {"account"})
)
@SQLDelete(sql = "UPDATE auth_user SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseAuditEntity {

  @Column(name = "username", length = 120, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password; // BCrypt 암호화

  @Column(name = "account", length = 120)
  private String account;

  @Column(name = "email", length = 200)
  private String email;

  /** 비밀번호 해시(별도 API로 관리 권장) */
  @Column(name = "password_hash", length = 200)
  private String passwordHash;

  /** 0/1 */
  @Column(name = "status", nullable = false)
  private Integer status = 1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id")
  private Department department;


  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UserRole> userRoles = new HashSet<>();
}