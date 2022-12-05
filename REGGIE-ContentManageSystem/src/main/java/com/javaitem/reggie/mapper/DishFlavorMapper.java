package com.javaitem.reggie.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.javaitem.reggie.domain.DishFlavor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.javaitem.reggie.domain.DishFlavor
 */
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
    List<DishFlavor> selectAllByDishId(@Param("dishId") Long dishId);

    int deleteByDishId(@Param("dishId") Long dishId);

}




