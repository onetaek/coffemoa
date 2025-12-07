package com.coffemoa.global.domain.user;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "auth_department",
    indexes = @Index(name = "ix_dept_parent", columnList = "parent_id"),
    uniqueConstraints = @UniqueConstraint(name = "uk_dept_name", columnNames = {"department_name"})
)
@SQLDelete(sql = "UPDATE auth_department SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Department extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "department_name", length = 150, nullable = false)
  private String departmentName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Department parent;

  @OneToMany(mappedBy = "parent")
  @Where(clause = "is_active = true")
  private List<Department> children = new ArrayList<>();

  @Column(name = "tree_path", length = 500)
  private String treePath;

  /**
   * 0/1
   */
  @Column(name = "status", nullable = false)
  private Integer status = 1;
}