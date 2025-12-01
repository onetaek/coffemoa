package com.coffemoa.domain.standard.cafemenuprice;

import com.coffemoa.domain.standard.cafemenu.CafeMenu;
import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "cafe_menu_price",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_cafe_menu_price",
        columnNames = {"cafe_menu_id", "temperature", "size", "deleted_at"}
    )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuPrice extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 어떤 메뉴의 가격인지
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cafe_menu_id", nullable = false)
  private CafeMenu cafeMenu;

  /**
   * 온도 옵션
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "temperature", nullable = false)
  private TemperatureOption temperature;

  /**
   * 사이즈 옵션
   */
  @Enumerated(EnumType.STRING)
  @Column(name = "size", nullable = false)
  private SizeOption size;

  /**
   * 판매 가격
   */
  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "code")
  private String code;
}

