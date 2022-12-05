package com.javaitem.reggie.service;

import com.javaitem.reggie.domain.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 *
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    BigDecimal sumAmount(Long userId);


}
