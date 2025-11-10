package com.coffemoa.domain.standard.mapper;

import com.coffemoa.domain.standard.entity.CafeMenu;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CafeMenuMapper {

  void insert(Map<String, String> param);

  List<CafeMenu> select(Map<String, String> param);

  void update(Map<String, String> param);

  void delete(Map<String, String> param);

}
