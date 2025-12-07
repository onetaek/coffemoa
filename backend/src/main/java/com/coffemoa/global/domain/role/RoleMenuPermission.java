package com.coffemoa.global.domain.role;

import com.coffemoa.global.BaseAuditEntity;
import com.coffemoa.global.domain.menu.MenuPermission;
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
    name = "auth_role_menu_permission",
    uniqueConstraints = @UniqueConstraint(name = "uk_role_menu_perm", columnNames = {"role_id",
        "menu_permission_id"}),
    indexes = {
        @Index(name = "ix_role_menu_perm_role", columnList = "role_id"),
        @Index(name = "ix_role_menu_perm_perm", columnList = "menu_permission_id")
    }
)
@SQLDelete(sql = "UPDATE auth_role_menu_permission SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoleMenuPermission extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_permission_id", nullable = false)
  private MenuPermission menuPermission;
}