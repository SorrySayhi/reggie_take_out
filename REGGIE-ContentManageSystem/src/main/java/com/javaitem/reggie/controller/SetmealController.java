package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaitem.reggie.domain.*;
import com.javaitem.reggie.dto.DishDto;
import com.javaitem.reggie.dto.SetmealDto;
import com.javaitem.reggie.service.CategoryService;
import com.javaitem.reggie.service.DishService;
import com.javaitem.reggie.service.SetmealDishService;
import com.javaitem.reggie.service.SetmealService;
import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author win
 * @create 2022/11/24 15:02
 */
@RestController
@Slf4j
@RequestMapping(value = {"/backend/setmeal","/front/setmeal"})
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    public CommonResult<Page> pageCommonResult(int page, int pageSize, @RequestParam(required = false)String name){
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name).orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> collect = records.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        setmealDtoPage.setRecords(collect);
        return CommonResult.success(setmealDtoPage);

    }
    @PostMapping("/status/{status}")
    public CommonResult<Setmeal> updateStatus(@PathVariable int status , String ids){
        String[] split = ids.split(",");
        List<Long> collect = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        for (Long id:collect
             ) {
            setmealService.updateStatusById(status,id);
        }
        return CommonResult.success(null);
    }

    @DeleteMapping
    public CommonResult<Setmeal> delete(@RequestParam List<Long> ids){

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1).in(Setmeal::getId,ids);
        long count = setmealService.count(setmealLambdaQueryWrapper);
        if (count>0){
            return CommonResult.error("在售中");
        }
        boolean b = setmealService.removeBatchByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        boolean remove = setmealDishService.remove(queryWrapper);
        if (!b&&remove){
            return CommonResult.error("删除失败");
        }
        return CommonResult.success(null);
    }

    @PostMapping
    public CommonResult<SetmealDto> addSetmeal(@RequestBody SetmealDto setmealDto){
        boolean save = setmealService.save(setmealDto);
        List<SetmealDish> collect = setmealDto.getSetmealDishes().stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        boolean b = setmealDishService.saveBatch(collect);
        if (save&&b){
            return CommonResult.success(null);
        }
        return CommonResult.error("失败 ");
    }

    @GetMapping("/{id}")
    @Transactional
    public CommonResult<Setmeal> getSetmeal(@PathVariable Long id){
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return CommonResult.success(setmealDto);
    }

    @GetMapping("/list")
    public CommonResult<List<Setmeal>> listCommonResult(Long categoryId, Integer status){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,categoryId).eq(Setmeal::getStatus,status);
        List<Setmeal> list = setmealService.list(queryWrapper);
        if (list==null){
            return CommonResult.error("error");
        }
        return CommonResult.success(list);

    }

    @GetMapping("/dish/{id}")
    public CommonResult<List>  getSetmealDish(@PathVariable Long id){
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        return CommonResult.success(list);
    }


}
