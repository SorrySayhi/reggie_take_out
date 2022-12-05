package com.javaitem.reggie.mapper;
import org.apache.ibatis.annotations.Param;

import com.javaitem.reggie.domain.Setmeal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.javaitem.reggie.domain.Setmeal
 */
public interface SetmealMapper extends BaseMapper<Setmeal> {

    int updateStatusById(@Param("status") Integer status, @Param("id") Long id);

}




