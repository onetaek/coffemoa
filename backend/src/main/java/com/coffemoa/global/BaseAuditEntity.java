package com.coffemoa.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 사용 여부 (soft delete) */
  @Column(name = "is_active", nullable = false)
  private Boolean isActive = true;

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

  /** 삭제한 사용자(soft delete) */
  @Column(name = "deleted_by")
  private String deletedBy;

  /** soft delete 시각 */
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  // (안전망) DB/빌더 어떤 경로로든 null이면 true로 보정
  @PrePersist
  protected void prePersist() {
    if (isActive == null) isActive = true;
  }

  /** ✅ 소프트 삭제: 실행자(actor)를 기록하고 비활성화 */
  public void markDeleted(String actor) {
    this.isActive = false;
    this.deletedBy = actor;
    this.deletedAt = LocalDateTime.now();
  }

  /** ✅ 복구(옵션): 삭제표시 해제 */
  public void restore() {
    this.isActive = true;
    this.deletedBy = null;
    this.deletedAt = null;
  }
}