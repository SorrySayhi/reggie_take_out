package com.javaitem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaitem.reggie.domain.DishFlavor;
import com.javaitem.reggie.service.DishFlavorService;
import com.javaitem.reggie.mapper.DishFlavorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public List<DishFlavor> selectAllByDishId(Long dishId) {
        return dishFlavorMapper.selectAllByDishId(dishId);
    }

    @Override
    public int deleteByDishId(Long dishId) {
        return dishFlavorMapper.deleteByDishId(dishId);
    }
}




