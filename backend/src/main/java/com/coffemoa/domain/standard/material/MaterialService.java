package com.coffemoa.domain.standard.material;

import com.coffemoa.domain.standard.unit.Unit;
import com.coffemoa.domain.standard.unit.UnitRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialService {

  private final MaterialRepository materialRepository;
  private final UnitRepository unitRepository;

  @Transactional(readOnly = true)
  public List<MaterialResponse> getList(MaterialSearchRequest request) {

    return materialRepository.search(request).stream()
        .map(m -> MaterialResponse.builder()
            .id(m.getId())
            .name(m.getName())
            .purchaseUnitId(m.getPurchaseUnit() != null ? m.getPurchaseUnit().getId() : null)
            .purchaseQuantity(m.getPurchaseQuantity())
            .purchasePrice(m.getPurchasePrice())
            .createdAt(m.getCreatedAt())
            .updatedAt(m.getUpdatedAt())
            .build()
        ).toList();
  }

  public void process(List<MaterialCUDRequest> list) {
    for (MaterialCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(MaterialCUDRequest req) {

    Unit unit = null;

    if (req.getPurchaseUnitId() != null) {
      unit = unitRepository.findById(req.getPurchaseUnitId())
          .orElseThrow(() -> new RuntimeException("Unit not found"));
    }

    Material material = Material.builder()
        .name(req.getName())
        .purchaseUnit(unit)
        .purchaseQuantity(req.getPurchaseQuantity())
        .purchasePrice(req.getPurchasePrice())
        .build();

    materialRepository.save(material);
  }

  private void update(MaterialCUDRequest req) {

    Material material = materialRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("Material not found"));

    Unit unit = null;

    if (req.getPurchaseUnitId() != null) {
      unit = unitRepository.findById(req.getPurchaseUnitId())
          .orElseThrow(() -> new RuntimeException("Unit not found"));
    }

    material.setName(req.getName());
    material.setPurchaseUnit(unit);
    material.setPurchaseQuantity(req.getPurchaseQuantity());
    material.setPurchasePrice(req.getPurchasePrice());
  }

  private void delete(MaterialCUDRequest req) {

    Material material = materialRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("Material not found"));

    material.markDeleted();
  }
}
