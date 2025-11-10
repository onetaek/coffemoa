package com.coffemoa.domain.standard.mapper;

import com.coffemoa.domain.standard.entity.Material;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialMapper {

  void insert(Map<String, String> param);

  List<Material> select(Map<String, String> param);

  void update(Map<String, String> param);

  void delete(Map<String, String> param);

}
