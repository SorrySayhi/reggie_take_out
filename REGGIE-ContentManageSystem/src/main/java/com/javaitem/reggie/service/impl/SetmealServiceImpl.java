package com.javaitem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaitem.reggie.domain.Setmeal;
import com.javaitem.reggie.service.SetmealService;
import com.javaitem.reggie.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
    implements SetmealService{
    @Autowired
    private SetmealMapper setmealMapper;



    @Override
    public int updateStatusById(Integer status, Long id) {
        return setmealMapper.updateStatusById(status, id);
    }


}




