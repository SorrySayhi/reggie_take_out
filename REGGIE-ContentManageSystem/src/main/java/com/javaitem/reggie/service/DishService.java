package com.javaitem.reggie.service;

import com.javaitem.reggie.domain.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.javaitem.reggie.dto.DishDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface DishService extends IService<Dish> {
    int updateStatusById(Integer status,Long id);
    List<Dish> selectAllByCategoryId(Long categoryId);

    List<DishDto> selectAll();

}
