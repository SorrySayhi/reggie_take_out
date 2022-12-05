package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaitem.reggie.domain.Category;
import com.javaitem.reggie.domain.Dish;
import com.javaitem.reggie.domain.Setmeal;
import com.javaitem.reggie.service.CategoryService;
import com.javaitem.reggie.service.DishService;
import com.javaitem.reggie.service.SetmealService;
import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.COMM_FAILURE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author win
 * @create 2022/11/24 7:18
 */
@RestController
@Slf4j
@RequestMapping(value = {"/backend/category","/front/category"})
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public CommonResult<Page> categoryList(Integer page,Integer pageSize){
        Page<Category> objectPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(true,Category::getSort);
        categoryService.page(objectPage,queryWrapper);
        return CommonResult.success(objectPage);


    }
    @PostMapping
    public CommonResult<Category> addCategory(@RequestBody Category category){
        boolean save = categoryService.save(category);
        if (!save){
            return CommonResult.error("重试");
        }
        return CommonResult.success(category);
    }

    @DeleteMapping
    public CommonResult<Category> deleteCategory(@RequestParam("ids") Long id){
        Category category = categoryService.getById(id);
        Integer type = category.getType();
        if (type==1){
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(true,Dish::getCategoryId,id);
            long count = dishService.count(queryWrapper);
            if (count>0){
                return CommonResult.error("该分类不可删除！");
            }
        }else{
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(true,Setmeal::getCategoryId,id);
            long count = setmealService.count(queryWrapper);
            if (count>0){
                return CommonResult.error("该分类不可删除！");
            }
        }

        boolean b = categoryService.removeById(id);
        if (!b){
            return CommonResult.error("重试");
        }
        return CommonResult.success(null);
    }

    @PutMapping
    public CommonResult<Category> updateCategory(@RequestBody Category category){

        boolean b = categoryService.updateById(category);
        if (!b){
            return CommonResult.error("重试");
        }
        return CommonResult.success(null);
    }
    @GetMapping("/list")
    public CommonResult<List> list(@RequestParam(required = false) Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(type!=null,Category::getType,type);
        List<Category> categories = categoryService.list(queryWrapper);
        return CommonResult.success(categories);

    }
}
