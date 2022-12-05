package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


import com.javaitem.reggie.domain.Employee;

import com.javaitem.reggie.service.EmployeeService;

import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author win
 * @create 2022/11/22 16:47
 */
//@Controller
@RestController
@Slf4j
@RequestMapping("/backend")
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee/login")
    public CommonResult login(HttpServletRequest request, @RequestBody Map user){
        String username =(String) user.get("username");

        Employee employee = employeeService.selectUsername(username);

        log.info(employee+"");
        if (employee==null){
            return CommonResult.error("账号不存在");
        }
        String password =(String) user.get("password");
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!employee.getPassword().equals(password)){
            return CommonResult.error("密码错误");
        }
        if (employee.getStatus()==0){
            return CommonResult.error("账号被禁用");
        }
        request.getSession().setAttribute("employee",employee.getId());
        return CommonResult.success(employee);
    }

    @RequestMapping("/employee/logout")
    public CommonResult logOut(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return CommonResult.success("退出成功");
    }
//    employee/page?page=1&pageSize=10
    @GetMapping("employee/page")
    public CommonResult<Page> employeePage(HttpServletRequest request,
                                           @RequestParam("page")int page, @RequestParam("pageSize")int pagesize,@RequestParam(value = "name",required = false)String name){
        Page<Employee> objectPage = new Page<>(page,pagesize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        employeeService.page(objectPage,queryWrapper);
//        request.getSession().setAttribute("");
        return CommonResult.success(objectPage);
    }

    @PostMapping("/employee")
    public CommonResult addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        LocalDateTime now = LocalDateTime.now();
        Long userId = (Long)request.getSession().getAttribute("employee");
//        employee.setCreateTime(now);
//        employee.setUpdateTime(now);
//        employee.setCreateUser(userId);
//        employee.setUpdateUser(userId);
        employeeService.addEmployee(employee);
        return CommonResult.success(employee);

    }
    @PutMapping("/employee")
    public CommonResult<Employee> UpdateEmployeeStatus(HttpServletRequest request,@RequestBody Employee employee){
        Long userId =(Long) request.getSession().getAttribute("employee");
        Long id = employee.getId();
        if (userId==id){
            return CommonResult.error("禁止禁用自己");
        }
        LocalDateTime now = LocalDateTime.now();
        employee.setUpdateUser(userId);
        employee.setUpdateTime(now);
        employeeService.updateById(employee);
        return CommonResult.success(null);
    }
    @GetMapping("/employee/{id}")
    public CommonResult<Employee> selectEmployeeById(@PathVariable("id")Long id){
        Employee employee = employeeService.getById(id);
        return CommonResult.success(employee);
    }
}
