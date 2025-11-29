package com.coffemoa.domain.standard.menurecipe;

import java.util.List;

public interface MenuRecipeQueryRepository {

  List<MenuRecipe> search(MenuRecipeSearchRequest request);
}
