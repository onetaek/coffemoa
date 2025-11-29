package com.coffemoa.domain.standard.menurecipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRecipeRepository
    extends JpaRepository<MenuRecipe, Long>, MenuRecipeQueryRepository {

}
