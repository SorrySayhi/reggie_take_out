package com.javaitem.reggie.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.javaitem.reggie.domain.Employee;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
public interface EmployeeService extends IService<Employee> {
    Employee selectUsername(String username);
    List selectEmployee(String username, String password);
    Integer selectStatus(String username);

    List<Employee> selectList();




    Integer addEmployee(Employee employee);
}
