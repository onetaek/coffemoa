package com.coffemoa.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseAuditEntity {

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @CreatedBy
  @Column(name = "created_by", updatable = false)
  private String createdBy;

  @LastModifiedBy
  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Column(name = "deleted_by")
  private String deletedBy;

  /**
   * Soft Delete
   */
  public void markDeleted() {
    this.deletedAt = LocalDateTime.now();

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
      this.deletedBy = auth.getName();
    } else {
      this.deletedBy = "SYSTEM";
    }
  }

  /**
   * 복구
   */
  public void restore() {
    this.deletedAt = null;
    this.deletedBy = null;
  }
}

