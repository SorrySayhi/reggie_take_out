package com.javaitem.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaitem.reggie.domain.Dish;
import com.javaitem.reggie.domain.DishFlavor;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface DishFlavorService extends IService<DishFlavor> {
    List<DishFlavor> selectAllByDishId(Long dishId);

    int deleteByDishId(Long dishId);
}
