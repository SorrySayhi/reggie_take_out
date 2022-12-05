package com.javaitem.reggie.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.javaitem.reggie.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.javaitem.reggie.domain.Category
 */

public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> selectAllByType(@Param("type") Integer type);
}




