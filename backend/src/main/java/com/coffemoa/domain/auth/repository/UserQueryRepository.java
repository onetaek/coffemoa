package com.coffemoa.domain.auth.repository;

import com.coffemoa.domain.auth.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

  private final JPAQueryFactory query;

//  public Optional<User> findByUsername(String username) {
//    QUser user = QUser.user;
//
//    User found = query.selectFrom(user)
//        .where(
//            user.username.eq(username)
//                .and(user.deletedAt.isNotNull())
//        )
//        .fetchOne();
//    return Optional.ofNullable(found);
//  }

  public boolean existsByUsername(String username) {
    QUser user = QUser.user;

    Integer one = query.selectOne()
        .from(user)
        .where(user.username.eq(username)
            .and(user.deletedAt.isNull()))
        .fetchFirst();
    return one != null;
  }
//
//  public Set<String> findAuthorityStringsByUserId(Long userId) {
//    QUserRole ur = QUserRole.userRole;
//    QRole r = QRole.role;
//    QRoleMenuPermission rmp = QRoleMenuPermission.roleMenuPermission;
//    QMenuPermission mp = QMenuPermission.menuPermission;
//    QMenu m = QMenu.menu;
//
//    // concat( concat(authority_prefix, ':'), action )
//    StringTemplate authority =
//        Expressions.stringTemplate(
//            "concat(concat({0}, {1}), {2})",
//            m.authorityPrefix,
//            Expressions.constant(":"),
//            mp.value
//        );
//
//    return new HashSet<>(
//        query.select(authority)
//            .from(ur)
//            .join(ur.role, r).on(r.isActive.isTrue())
//            .join(rmp).on(rmp.role.eq(r), rmp.isActive.isTrue())
//            .join(mp).on(mp.eq(rmp.menuPermission), mp.isActive.isTrue())
//            .join(m).on(m.eq(mp.menu), m.isActive.isTrue())
//            .where(
//                ur.user.id.eq(userId),
//                ur.isActive.isTrue()
//            )
//            .fetch()
//    );
//  }
}