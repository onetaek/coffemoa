package com.coffemoa.global.domain.user;

import com.coffemoa.global.BaseAuditEntity;
import com.coffemoa.global.domain.role.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    name = "auth_user_role",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id",
        "role_id"}),
    indexes = {
        @Index(name = "ix_user_role_user", columnList = "user_id"),
        @Index(name = "ix_user_role_role", columnList = "role_id")
    }
)
@SQLDelete(sql = "UPDATE auth_user_role SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public final class UserRole extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;
}
