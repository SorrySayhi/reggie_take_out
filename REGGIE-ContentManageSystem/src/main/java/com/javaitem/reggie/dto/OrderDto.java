package com.javaitem.reggie.dto;

import com.javaitem.reggie.domain.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author win
 * @create 2022/11/26 20:32
 */
@Data
public class OrderDto {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private LocalDateTime orderTime;

    private Integer status;

    private List<OrderDetail> orderDetails;

    private BigDecimal amount;
}
