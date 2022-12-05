package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaitem.reggie.domain.ShoppingCart;
import com.javaitem.reggie.service.ShoppingCartService;
import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author win
 * @create 2022/11/26 10:18
 */
@RestController
@Slf4j
@RequestMapping("/front/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public CommonResult<List> getList(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return CommonResult.success(list);
    }

    @PostMapping("/add")
    public CommonResult add(HttpServletRequest request, @RequestBody ShoppingCart shoppingCart){
        Long id = (Long)request.getSession().getAttribute("user");
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(shoppingCart.getDishFlavor()),ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
        if (dishId==null){
            queryWrapper.eq(ShoppingCart::getUserId,id).eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else {
            queryWrapper.eq(ShoppingCart::getUserId,id).eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one!=null){
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
            return CommonResult.success(shoppingCart);
        }
        shoppingCart.setUserId(id);
        boolean save = shoppingCartService.save(shoppingCart);
        if (save){
            return CommonResult.success(shoppingCart);
        }else {
            return CommonResult.error("添加失败");
        }
    }

    @PostMapping("/sub")
    @Transactional
    public CommonResult sub(HttpServletRequest request,@RequestBody ShoppingCart shoppingCart){
        Long id = (Long)request.getSession().getAttribute("user");
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (dishId==null){
            queryWrapper.eq(ShoppingCart::getUserId,id).eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }else {
            queryWrapper.eq(ShoppingCart::getUserId,id).eq(ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor()).eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        int i = one.getNumber() - 1;
        if (i==0){
            boolean remove = shoppingCartService.remove(queryWrapper);
            if (!remove){
                return CommonResult.error("删除错误");
            }
            return CommonResult.success(one);
        }
        one.setNumber(i);
        boolean b = shoppingCartService.updateById(one);
        if (b){
            return CommonResult.success(shoppingCart);
        }else {
            return CommonResult.error("删除失败");
        }
    }

    @DeleteMapping("/clean")
    public CommonResult clean(HttpServletRequest request){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        Long user =(Long) request.getSession().getAttribute("user");
        queryWrapper.eq(ShoppingCart::getUserId,user);
        boolean remove = shoppingCartService.remove(queryWrapper);
        if (remove){
            return CommonResult.success(null);
        }else {
            return CommonResult.error("清空失败");
        }
    }

}
