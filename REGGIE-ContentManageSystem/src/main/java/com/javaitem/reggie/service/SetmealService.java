package com.javaitem.reggie.service;

import com.javaitem.reggie.domain.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.javaitem.reggie.dto.SetmealDto;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
public interface SetmealService extends IService<Setmeal> {
    int updateStatusById(Integer status,Long id);



}
