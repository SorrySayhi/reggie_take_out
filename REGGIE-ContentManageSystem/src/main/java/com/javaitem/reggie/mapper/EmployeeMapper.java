package com.javaitem.reggie.mapper;
import java.time.LocalDateTime;
import java.util.List;

import com.javaitem.reggie.domain.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Entity domain.domain.Employee
 */

public interface EmployeeMapper extends BaseMapper<Employee> {

    int updateStatusAndUpdateTimeAndUpdateUserById(@Param("status") Integer status, @Param("updateTime") LocalDateTime updateTime, @Param("updateUser") Long updateUser, @Param("id") Long id);

    List<Employee> selectNameAndUsernameAndPhoneAndStatus();

    Employee selectByUsername(@Param("username") String username);

    List<Employee> selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    Integer selectStatusByUsername(@Param("username") String username);

    Integer addAll(Employee employee);
}




