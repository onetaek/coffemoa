package com.coffemoa.domain.finance.fixedcost;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FixedCostService {

    private final FixedCostRepository repository;

    @Transactional(readOnly = true)
    public List<FixedCostResponse> getList(FixedCostSearchRequest request) {

        return repository.search(request).stream()
                .map(FixedCostResponse::fromEntity)
                .toList();
    }

    public void process(List<FixedCostCUDRequest> list) {

        for (FixedCostCUDRequest req : list) {
            switch (req.getFlag()) {
                case C -> create(req);
                case U -> update(req);
                case D -> delete(req);
            }
        }
    }

    private void create(FixedCostCUDRequest req) {

        FixedCost entity = FixedCost.builder()
                .costName(req.getCostName())
                .periodType(FixedCostPeriodType.fromCode(req.getPeriodTypeCode()))
                .remark(req.getRemark())
                .build();

        repository.save(entity);
    }

    private void update(FixedCostCUDRequest req) {

        FixedCost entity = repository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("FixedCost not found"));

        entity.setCostName(req.getCostName());
        entity.setPeriodType(FixedCostPeriodType.fromCode(req.getPeriodTypeCode()));
        entity.setRemark(req.getRemark());
    }

    private void delete(FixedCostCUDRequest req) {

        FixedCost entity = repository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("FixedCost not found"));

        entity.markDeleted();
    }
}
