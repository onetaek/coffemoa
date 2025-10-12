package com.coffemoa.domain.auth.entity;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
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
    name = "auth_menu",
    indexes = {
        @Index(name = "ix_menu_parent", columnList = "parent_id"),
        @Index(name = "ix_menu_path", columnList = "path"),
        @Index(name = "ix_menu_status", columnList = "status")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_menu_route_name", columnNames = {"route_name"})
    }
)
@SQLDelete(sql = "UPDATE auth_menu SET is_active=false, deleted_at=now() WHERE id=?")
@Where(clause = "is_active = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Menu extends BaseAuditEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 20, nullable = false)
  private MenuType type = MenuType.MENU;   // 0=目录 → DIRECTORY, 1=菜单 → MENU

  /** 계층 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Menu parent;

  @OneToMany(mappedBy = "parent")
  @Where(clause = "is_active = true")
  @OrderBy("sort_order ASC, id ASC")
  private List<Menu> children = new ArrayList<>();

  @Column(name = "tree_path", length = 500)
  private String treePath;

  @Column(name = "sort_order", nullable = false)
  private Integer sortOrder = 0;

  /** vue-router 필드 */
  @Column(name = "path", length = 200)
  private String path;                 // 'analysis' | '/authorization' 등

  @Column(name = "route_name", length = 120)
  private String routeName;            // keep-alive name (Unique)

  @Column(name = "component", length = 300)
  private String component;            // '#', '##', 'views/Authorization/Menu/Menu'

  @Column(name = "redirect", length = 200)
  private String redirect;

  /** meta */
  @Column(name = "meta_icon", length = 100)
  private String icon;

  @Column(name = "meta_title", length = 100)
  private String title;

  @Column(name = "meta_hidden")
  private Boolean hidden = false;

  @Column(name = "meta_always_show")
  private Boolean alwaysShow = false;

  @Column(name = "meta_no_cache")
  private Boolean noCache = false;

  @Column(name = "meta_breadcrumb")
  private Boolean breadcrumb = true;

  @Column(name = "meta_affix")
  private Boolean affix = false;

  @Column(name = "meta_no_tags_view")
  private Boolean noTagsView = false;

  @Column(name = "meta_active_menu", length = 200)
  private String activeMenu;

  @Column(name = "meta_can_to")
  private Boolean canTo = false;

  /** 외부 링크 여부 */
  @Column(name = "external_link")
  private Boolean externalLink = false;

  /** 상태(0/1) */
  @Column(name = "status", nullable = false)
  private Integer status = 1;

  /**
   * 권한 prefix: ex) "example:dialog"
   * 버튼(action)과 합쳐서 "example:dialog:add" 형태의 서버 권한키 생성에 사용
   */
  @Column(name = "authority_prefix", length = 120)
  private String authorityPrefix;
}