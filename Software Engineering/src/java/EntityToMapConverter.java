package com.qst;

import com.qst.entity.BaseEntity;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityToMapConverter {
  public static Map<String, Object> convertEntityToMap(Object entity)
      throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    Class<?> clazz = entity.getClass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      map.put(field.getName(), field.get(entity));
    }
    if (entity instanceof BaseEntity) {
      map.put("id", ((BaseEntity) entity).getId());
    }
    return map;
  }

  public static List<Map<String, Object>> convertEntityListToMapList(List<?> entityList)
      throws IllegalAccessException {
    List<Map<String, Object>> mapList = new ArrayList<>();
    for (Object entity : entityList) {
      mapList.add(convertEntityToMap(entity));
    }
    return mapList;
  }
}