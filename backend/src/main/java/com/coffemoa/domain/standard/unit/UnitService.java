package com.coffemoa.domain.standard.unit;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnitService {

  private final UnitRepository unitRepository;

  @Transactional(readOnly = true)
  public List<UnitResponse> getList(UnitSearchRequest request) {
    return unitRepository.search(request).stream()
        .map(UnitResponse::fromEntity)
        .toList();
  }

  public void process(List<UnitCUDRequest> list) {
    for (UnitCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(UnitCUDRequest req) {
    Unit unit = Unit.builder()
        .name(req.getName())
        .description(req.getDescription())
        .build();
    unitRepository.save(unit);
  }

  private void update(UnitCUDRequest req) {
    Unit unit = unitRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("Unit not found"));
    unit.setName(req.getName());
    unit.setDescription(req.getDescription());
  }

  private void delete(UnitCUDRequest req) {
    Unit unit = unitRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("Unit not found"));
    unit.markDeleted();
  }
}
