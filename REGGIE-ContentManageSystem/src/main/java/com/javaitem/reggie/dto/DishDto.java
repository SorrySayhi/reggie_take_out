package com.javaitem.reggie.dto;

import com.javaitem.reggie.domain.Dish;
import com.javaitem.reggie.domain.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author win
 * @create 2022/11/24 14:27
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors ;

    private String categoryName;

    private Integer copies;

}
