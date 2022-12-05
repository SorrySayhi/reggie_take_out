package com.javaitem.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.javaitem.reggie.domain.Employee;
import com.javaitem.reggie.mapper.EmployeeMapper;
import com.javaitem.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee selectUsername(String username) {
        return employeeMapper.selectByUsername(username);
    }

    @Override
    public List selectEmployee(String username, String password) {

        return employeeMapper.selectByUsernameAndPassword(username, password);
    }

    @Override
    public Integer selectStatus(String username) {
        return employeeMapper.selectStatusByUsername(username);
    }

    @Override
    public List<Employee> selectList() {
        return employeeMapper.selectNameAndUsernameAndPhoneAndStatus();
    }




    @Override
    public Integer addEmployee(Employee employee) {
        return employeeMapper.addAll(employee);
    }
}




