package com.coffemoa.domain.standard.unitconversion;

import com.coffemoa.domain.standard.unit.Unit;
import com.coffemoa.domain.standard.unit.UnitRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UnitConversionService {

  private final UnitConversionRepository unitConversionRepository;
  private final UnitRepository unitRepository;

  @Transactional(readOnly = true)
  public List<UnitConversionResponse> getList(UnitConversionSearchRequest request) {
    return unitConversionRepository.search(request).stream()
        .map(uc -> UnitConversionResponse.builder()
            .id(uc.getId())
            .baseUnitId(uc.getBaseUnit().getId())
            .targetUnitId(uc.getTargetUnit().getId())
            .ratio(uc.getRatio())
            .createdAt(uc.getCreatedAt())
            .updatedAt(uc.getUpdatedAt())
            .build()
        ).toList();
  }

  public void process(List<UnitConversionCUDRequest> list) {
    for (UnitConversionCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(UnitConversionCUDRequest req) {

    Unit base = unitRepository.findById(req.getBaseUnitId())
        .orElseThrow(() -> new RuntimeException("Base unit not found"));

    Unit target = unitRepository.findById(req.getTargetUnitId())
        .orElseThrow(() -> new RuntimeException("Target unit not found"));

    UnitConversion uc = UnitConversion.builder()
        .baseUnit(base)
        .targetUnit(target)
        .ratio(req.getRatio())
        .build();

    unitConversionRepository.save(uc);
  }

  private void update(UnitConversionCUDRequest req) {

    UnitConversion uc = unitConversionRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("UnitConversion not found"));

    Unit base = unitRepository.findById(req.getBaseUnitId())
        .orElseThrow(() -> new RuntimeException("Base unit not found"));

    Unit target = unitRepository.findById(req.getTargetUnitId())
        .orElseThrow(() -> new RuntimeException("Target unit not found"));

    uc.setBaseUnit(base);
    uc.setTargetUnit(target);
    uc.setRatio(req.getRatio());
  }

  private void delete(UnitConversionCUDRequest req) {
    UnitConversion uc = unitConversionRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("UnitConversion not found"));

    uc.markDeleted();
  }
}
