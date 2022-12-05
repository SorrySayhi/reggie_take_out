package com.javaitem.reggie.dto;

import com.javaitem.reggie.domain.Setmeal;
import com.javaitem.reggie.domain.SetmealDish;
import lombok.Data;

import java.util.List;

/**
 * @Author win
 * @create 2022/11/25 7:29
 */
@Data
public class SetmealDto extends Setmeal {
   private List<SetmealDish> setmealDishes ;

    private String categoryName;


}
