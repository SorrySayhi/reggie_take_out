package com.javaitem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaitem.reggie.dto.OrderDto;
import com.javaitem.reggie.domain.Orders;
import com.javaitem.reggie.mapper.OrderDetailMapper;
import com.javaitem.reggie.service.OrderDetailService;
import com.javaitem.reggie.service.OrdersService;
import com.javaitem.reggie.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{
    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;


}




