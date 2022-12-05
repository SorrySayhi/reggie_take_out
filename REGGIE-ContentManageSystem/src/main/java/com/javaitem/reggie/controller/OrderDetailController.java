package com.javaitem.reggie.controller;

import com.javaitem.reggie.service.OrderDetailService;
import com.javaitem.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author win
 * @create 2022/11/26 20:24
 */
//@RestController
@Slf4j
//@RequestMapping("/front/order")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrdersService ordersService;




}
