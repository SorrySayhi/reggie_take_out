package com.javaitem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaitem.reggie.domain.Dish;
import com.javaitem.reggie.dto.DishDto;
import com.javaitem.reggie.mapper.DishFlavorMapper;
import com.javaitem.reggie.service.DishService;
import com.javaitem.reggie.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{
    @Autowired
    private DishMapper dishMapper;



    @Override
    public int updateStatusById(Integer status, Long id) {
        return dishMapper.updateStatusById(status, id);
    }

    @Override
    public List<Dish> selectAllByCategoryId(Long categoryId) {
        return dishMapper.selectAllByCategoryId(categoryId);
    }

    @Override
    public List<DishDto> selectAll() {
        return dishMapper.selectAll();
    }
}




