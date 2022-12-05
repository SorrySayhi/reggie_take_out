package com.javaitem.reggie.mapper;

import com.javaitem.reggie.domain.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @Entity com.javaitem.reggie.domain.ShoppingCart
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    BigDecimal sumAmount(@Param("userId") Long userId);

}




