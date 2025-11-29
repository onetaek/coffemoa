package com.coffemoa.domain.standard.cafemenu;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CafeMenuService {

  private final CafeMenuRepository cafeMenuRepository;

  @Transactional(readOnly = true)
  public List<CafeMenuResponse> getList(CafeMenuSearchRequest request) {
    return cafeMenuRepository.search(request).stream()
        .map(CafeMenuResponse::fromEntity)
        .toList();
  }

  public void process(List<CafeMenuCUDRequest> list) {

    for (CafeMenuCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(CafeMenuCUDRequest req) {
    CafeMenu menu = CafeMenu.builder()
        .name(req.getName())
        .build();
    cafeMenuRepository.save(menu);
  }

  private void update(CafeMenuCUDRequest req) {
    CafeMenu menu = cafeMenuRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("CafeMenu not found"));

    menu.setName(req.getName());
  }

  private void delete(CafeMenuCUDRequest req) {
    CafeMenu menu = cafeMenuRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("CafeMenu not found"));

    menu.markDeleted(); // 내부에서 SecurityContextHolder 사용
  }
}
