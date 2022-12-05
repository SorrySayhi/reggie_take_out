package com.javaitem.reggie;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author win
 * @create 2022/11/22 14:10
 */
@SpringBootApplication
@MapperScan({"com.javaitem.reggie.mapper"})
@EnableTransactionManagement
public class reggieMain {
    public static void main(String[] args) {
        SpringApplication.run(reggieMain. class,args);

    }
}
