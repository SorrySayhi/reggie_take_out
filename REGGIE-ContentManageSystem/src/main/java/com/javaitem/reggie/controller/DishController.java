package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.javaitem.reggie.domain.Category;
import com.javaitem.reggie.domain.Dish;
import com.javaitem.reggie.domain.DishFlavor;
import com.javaitem.reggie.domain.SetmealDish;
import com.javaitem.reggie.dto.DishDto;
import com.javaitem.reggie.service.CategoryService;
import com.javaitem.reggie.service.DishFlavorService;
import com.javaitem.reggie.service.DishService;
import com.javaitem.reggie.service.SetmealDishService;
import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author win
 * @create 2022/11/24 9:44
 */
@RestController
@Slf4j
@RequestMapping(value = {"/backend/dish","/front/dish"})
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/page")
    public CommonResult<Page>  pageCommonResult(int page,int pageSize,@RequestParam(required = false)String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        dishService.page(dishPage, queryWrapper);
        List<Dish> records = dishPage.getRecords();
        List<DishDto> collect = records.stream().map(item -> {
                    Long categoryId = item.getCategoryId();
                    Category category = categoryService.getById(categoryId);
                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);
                    dishDto.setCategoryName(category.getName());
                    return dishDto;
                }
        ).collect(Collectors.toList());
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        dishDtoPage.setRecords(collect);
        return CommonResult.success(dishDtoPage);
    }
    @GetMapping("/{id}")
    @Transactional
    public CommonResult<DishDto> getDish(@PathVariable Long id){
        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        List<DishFlavor> dishFlavors = dishFlavorService.selectAllByDishId(id);
        dishDto.setFlavors(dishFlavors);
        return CommonResult.success(dishDto);
    }

    @PostMapping
    @Transactional
    public CommonResult<Dish> addDish(@RequestBody DishDto dishDto){
        boolean save = dishService.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (!flavors.isEmpty()){
            flavors = flavors.stream().map((item) -> {item.setDishId(id);return item;}).collect(Collectors.toList());

            boolean b = dishFlavorService.saveBatch(flavors);
            if (!b){
                return CommonResult.error("????????????");
            }
        }
        log.info(save+"");
        if (!save){
            log.info("shibai");
            return CommonResult.error("??????");

        }
        String dishRedisKey = "dish_"+dishDto.getCategoryId()+dishDto.getStatus();
        redisTemplate.delete(dishRedisKey);
        return CommonResult.success(null);
    }
    @DeleteMapping()
    public CommonResult<Dish> deleteDish(String ids){

        String[] split = ids.split(",");
        List<Long> collect = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        List<Long> badId =new ArrayList<>();
        for (Long id:collect
             ) {
            Dish dish = dishService.getById(id);
            Integer status = dish.getStatus();
            if (status==1){
                badId.add(id);
                continue;
            }
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getDishId,id);
            long count = setmealDishService.count(queryWrapper);
            if (count>0){
                return CommonResult.error("??????????????????"+id+"?????????");
            }
        }
        if (!badId.isEmpty()){
            collect.removeAll(badId);
            if (collect.isEmpty()){
                return CommonResult.error("??????????????????");
            }
        }

        boolean b = dishService.removeBatchByIds(collect);
        for (Long id:collect){
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,id);
            dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        }
        if (b){
            if (badId!=null){
                return CommonResult.error("??????????????????");
            }
            return CommonResult.success(null);
        }else {
            return CommonResult.error("??????");
        }

    }

    @PostMapping("/status/{status}")
    public CommonResult<Dish> updateStatus(@PathVariable int status,@RequestParam("ids")String ids){
        String[] split = ids.split(",");
        List<Long> collect = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        for (Long id:collect
             ) {
            dishService.updateStatusById(status,id);
        }
        Set keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        return CommonResult.success(null);
    }

    @GetMapping("/list")
    public CommonResult<List> dishList(HttpServletRequest request,Dish dish){
        String categoryId =dish.getCategoryId().toString();
        String dishRedisKey = "dish_"+categoryId+dish.getStatus();

        Boolean aBoolean = redisTemplate.hasKey(dishRedisKey);
        if (aBoolean){
            List range = redisTemplate.opsForList().range(dishRedisKey, 0, -1);
            return CommonResult.success(range);
        }
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName()).eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId()).eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);

        String[] split = request.getRequestURI().split("/");
        if (split[1].equals("front")){
            List<DishDto> collect = list.stream().map(item -> {
                Long id = item.getId();
                LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
                dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
                List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(item, dishDto);
                dishDto.setFlavors(dishFlavorList);
                return dishDto;
            }).collect(Collectors.toList());
            if (collect==null){
                CommonResult.error("?????????");
            }
            redisTemplate.opsForList().leftPushAll(dishRedisKey,collect.toArray());

            return CommonResult.success(collect);
        }
        if (list==null){
            CommonResult.error("?????????");
        }
        return CommonResult.success(list);
    }

    @PutMapping
    @Transactional
    public CommonResult<DishDto> updateDish(@RequestBody DishDto dishDto){
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors= flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishService.updateById(dishDto);
        dishFlavorService.deleteByDishId(dishDto.getId());
        dishFlavorService.saveBatch(flavors);
        String dishRedisKey = "dish_"+dishDto.getCategoryId()+dishDto.getStatus();

        redisTemplate.delete(dishRedisKey);
        return CommonResult.success(null);
    }
}
