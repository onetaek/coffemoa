package com.coffemoa.domain.standard.cafemenuprice;

import com.coffemoa.domain.standard.cafemenu.CafeMenu;
import com.coffemoa.domain.standard.cafemenu.CafeMenuRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CafeMenuPriceService {

  private final CafeMenuPriceRepository priceRepository;
  private final CafeMenuRepository menuRepository;

  @Transactional(readOnly = true)
  public List<CafeMenuPriceResponse> getList(CafeMenuPriceSearchRequest request) {
    return priceRepository.search(request).stream()
        .map(CafeMenuPriceResponse::fromEntity)
        .toList();
  }

  public void process(List<CafeMenuPriceCUDRequest> list) {
    for (CafeMenuPriceCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(CafeMenuPriceCUDRequest req) {

    CafeMenu menu = menuRepository.findById(req.getCafeMenuId())
        .orElseThrow(() -> new RuntimeException("CafeMenu not found"));

    CafeMenuPrice price = CafeMenuPrice.builder()
        .cafeMenu(menu)
        .temperature(req.getTemperature())
        .size(req.getSize())
        .price(req.getPrice())
        .build();

    priceRepository.save(price);
  }

  private void update(CafeMenuPriceCUDRequest req) {

    CafeMenuPrice price = priceRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("CafeMenuPrice not found"));

    CafeMenu menu = menuRepository.findById(req.getCafeMenuId())
        .orElseThrow(() -> new RuntimeException("CafeMenu not found"));

    price.setCafeMenu(menu);
    price.setTemperature(req.getTemperature());
    price.setSize(req.getSize());
    price.setPrice(req.getPrice());
  }

  private void delete(CafeMenuPriceCUDRequest req) {
    CafeMenuPrice price = priceRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("CafeMenuPrice not found"));

    price.markDeleted();
  }
}
