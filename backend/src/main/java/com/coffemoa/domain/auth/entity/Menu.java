package com.coffemoa.domain.auth.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 메뉴 (기능 단위)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "menus")
public class Menu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name; // ex) 커피 관리

  @Column(length = 100)
  private String code; // ex) MENU_COFFEE_MGMT

  @Column(length = 200)
  private String path; // ex) /coffee or /api/coffee

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<MenuPermission> menuPermissions = new HashSet<>();
}

