package com.javaitem.reggie.mapper;
import java.util.List;

import com.javaitem.reggie.dto.DishDto;
import org.apache.ibatis.annotations.Param;

import com.javaitem.reggie.domain.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.javaitem.reggie.domain.Dish
 */
public interface DishMapper extends BaseMapper<Dish> {
    int updateStatusById(@Param("status") Integer status, @Param("id") Long id);

    List<Dish> selectAllByCategoryId(@Param("categoryId") Long categoryId);

    List<DishDto> selectAll();


}




