package com.coffemoa.domain.standard.menurecipe;

import com.coffemoa.domain.standard.cafemenuprice.CafeMenuPrice;
import com.coffemoa.domain.standard.cafemenuprice.CafeMenuPriceRepository;
import com.coffemoa.domain.standard.material.Material;
import com.coffemoa.domain.standard.material.MaterialRepository;
import com.coffemoa.domain.standard.unit.Unit;
import com.coffemoa.domain.standard.unit.UnitRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuRecipeService {

  private final MenuRecipeRepository recipeRepository;
  private final CafeMenuPriceRepository priceRepository;
  private final MaterialRepository materialRepository;
  private final UnitRepository unitRepository;

  @Transactional(readOnly = true)
  public List<MenuRecipeResponse> getList(MenuRecipeSearchRequest request) {

    return recipeRepository.search(request).stream()
        .map(MenuRecipeResponse::fromEntity)
        .toList();
  }

  public void process(List<MenuRecipeCUDRequest> list) {
    for (MenuRecipeCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(MenuRecipeCUDRequest req) {

    CafeMenuPrice price = priceRepository.findById(req.getCafeMenuPriceId())
        .orElseThrow(() -> new RuntimeException("CafeMenuPrice not found"));

    Material material = materialRepository.findById(req.getMaterialId())
        .orElseThrow(() -> new RuntimeException("Material not found"));

    Unit unit = unitRepository.findById(req.getUnitId())
        .orElseThrow(() -> new RuntimeException("Unit not found"));

    MenuRecipe r = MenuRecipe.builder()
        .cafeMenuPrice(price)
        .material(material)
        .usageAmount(req.getUsageAmount())
        .unit(unit)
        .build();

    recipeRepository.save(r);
  }

  private void update(MenuRecipeCUDRequest req) {

    MenuRecipe r = recipeRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("MenuRecipe not found"));

    CafeMenuPrice price = priceRepository.findById(req.getCafeMenuPriceId())
        .orElseThrow(() -> new RuntimeException("CafeMenuPrice not found"));

    Material material = materialRepository.findById(req.getMaterialId())
        .orElseThrow(() -> new RuntimeException("Material not found"));

    Unit unit = unitRepository.findById(req.getUnitId())
        .orElseThrow(() -> new RuntimeException("Unit not found"));

    r.setCafeMenuPrice(price);
    r.setMaterial(material);
    r.setUsageAmount(req.getUsageAmount());
    r.setUnit(unit);
  }

  private void delete(MenuRecipeCUDRequest req) {

    MenuRecipe r = recipeRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("MenuRecipe not found"));

    r.markDeleted();
  }
}
