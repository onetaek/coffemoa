package com.coffemoa.domain.standard.mapper;

import com.coffemoa.domain.standard.entity.CafeMenuPrice;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CafeMenuPriceMapper {

  void insert(Map<String, String> param);

  List<CafeMenuPrice> select(Map<String, String> param);

  void update(Map<String, String> param);

  void delete(Map<String, String> param);

}
