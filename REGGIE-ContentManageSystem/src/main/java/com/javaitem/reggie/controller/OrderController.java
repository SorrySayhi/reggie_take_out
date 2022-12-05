package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaitem.reggie.domain.*;
import com.javaitem.reggie.dto.OrderDto;
import com.javaitem.reggie.service.*;
import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Author win
 * @create 2022/11/25 8:30
 */
@RestController
@Slf4j
@RequestMapping(value = {"/backend/order","/front/order"})
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/page")
    public CommonResult<Page> ordersPage(int page, int pageSize,
                                         @RequestParam(required = false)String number,
                                         @RequestParam(required = false)String beginTime,
                                         @RequestParam(required = false)String endTime){
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        LocalDateTime begin =null;
        LocalDateTime end = null;
        boolean b = StringUtils.isNotEmpty(beginTime) && (StringUtils.isNotEmpty(endTime));
        if (b){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            begin = LocalDateTime.parse(beginTime, dateTimeFormatter);
             end = LocalDateTime.parse(endTime, dateTimeFormatter);
        }
        queryWrapper.like(StringUtils.isNotEmpty(number),Orders::getNumber,number)
                .between(b,Orders::getOrderTime,begin,end)
    .orderByDesc(Orders::getCheckoutTime);
        ordersService.page(ordersPage, queryWrapper);
        return CommonResult.success(ordersPage);
    }

    @GetMapping("/userPage")
    public CommonResult<Page<OrderDto>> userPage(HttpServletRequest request,int page, int pageSize){
        Long user = (Long)request.getSession().getAttribute("user");
        Page<Orders> ordersPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders::getUserId,user).orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage,ordersLambdaQueryWrapper);
        List<Orders> records = ordersPage.getRecords();
        List<OrderDto> collect = records.stream().map(item -> {
            Long id = item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, id);
            List<OrderDetail> list = orderDetailService.list(queryWrapper);
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            orderDto.setOrderDetails(list);
            return orderDto;
        }).collect(Collectors.toList());

        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPage,orderDtoPage,"records");
        orderDtoPage.setRecords(collect);
        return CommonResult.success(orderDtoPage);

    }


    @PostMapping("/submit")
    @Transactional
    public CommonResult<OrderDto> newOrder(HttpServletRequest request,@RequestBody Orders orders){
        Long user =(Long) request.getSession().getAttribute("user");
        long id = IdWorker.getId();
        orders.setNumber(String.valueOf(id));
        orders.setUserId(user);
        User byId = userService.getById(user);
        orders.setUserName(byId.getName());
        orders.setPhone(byId.getPhone());
        AddressBook byId1 = addressBookService.getById(orders.getAddressBookId());
        orders.setAddress(byId1.getDetail());
        orders.setConsignee(byId1.getConsignee());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
//        ordersService.save()
        //查找购物车
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,user);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartLambdaQueryWrapper);
        //提交订单
        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<OrderDetail> collect = shoppingCartList.stream().map(item -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail);
            orderDetail.setOrderId(id);
            orderDetailService.save(orderDetail);
            atomicInteger.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        orders.setAmount(new BigDecimal(atomicInteger.get()));
        ordersService.save(orders);
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orders,orderDto);
        orderDto.setOrderDetails(collect);
        //清空购物车
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return CommonResult.success(orderDto);
    }

    @PostMapping("/again")
    public CommonResult again(){

        return CommonResult.success(null);
    }



}
