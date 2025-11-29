package com.coffemoa.domain.auth.entity;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
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
    name = "auth_menu_permission",
    uniqueConstraints = @UniqueConstraint(name = "uk_menu_perm_value", columnNames = {"menu_id",
        "value"}),
    indexes = @Index(name = "ix_menu_perm_menu", columnList = "menu_id")
)
@SQLDelete(sql = "UPDATE auth_menu_permission SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuPermission extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "menu_id", nullable = false)
  private Menu menu;

  /**
   * 프론트 value(ex. add, edit, delete)
   */
  @Column(name = "_value", length = 60, nullable = false)
  private String value;

  /**
   * 프론트 label(ex. 新增, 编辑…)
   */
  @Column(name = "label", length = 100, nullable = false)
  private String label;
}