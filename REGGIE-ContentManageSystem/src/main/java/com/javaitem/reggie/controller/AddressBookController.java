package com.javaitem.reggie.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.javaitem.reggie.domain.AddressBook;
import com.javaitem.reggie.service.AddressBookService;
import com.reggie.util.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author win
 * @create 2022/11/26 13:44
 */
@RestController
@Slf4j
@RequestMapping("/front/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/default")
    public CommonResult<AddressBook> defaultAddress(HttpServletRequest request){
        Long user = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,user).eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(queryWrapper);
        if (one ==null){
            return CommonResult.error("重试");
        }
        return CommonResult.success(one);


    }

    @PutMapping("/default")
    @Transactional
    public CommonResult<AddressBook> updateAddress(HttpServletRequest request,@RequestBody AddressBook addressBook){
        Long user = (Long) request.getSession().getAttribute("user");
        addressBookService.updateIsDefaultByUserIdAndIsDefault(false,user,true);
        addressBook.setIsDefault(true);
        addressBookService.updateById(addressBook);
        return CommonResult.success(addressBook);
    }

    @GetMapping("/list")
    public CommonResult<List<AddressBook>> addressList(HttpServletRequest request){
        Long user = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,user).orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        if (list==null){
            return CommonResult.error("重试");
        }
        return CommonResult.success(list);
    }

    @PostMapping
    public CommonResult<AddressBook> add(HttpServletRequest request,@RequestBody AddressBook addressBook){
        Long user = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(user);
        addressBook.setIsDefault(true);
        addressBookService.updateIsDefaultByUserIdAndIsDefault(false,user,true);
        boolean save = addressBookService.save(addressBook);
        if (!save){
            return CommonResult.error("error");
        }
        return CommonResult.success(addressBook);
    }

    @GetMapping("/{id}")
    public CommonResult<AddressBook> update(@PathVariable Long id){
        AddressBook byId = addressBookService.getById(id);
        if (byId==null){
            CommonResult.error("error");
        }
        return  CommonResult.success(byId);
    }
    
    
}
