package com.javaitem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javaitem.reggie.domain.AddressBook;
import com.javaitem.reggie.service.AddressBookService;
import com.javaitem.reggie.mapper.AddressBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public int updateIsDefaultByUserIdAndIsDefault(Boolean isDefault, Long userId, Boolean oldIsDefault) {
        return addressBookMapper.updateIsDefaultByUserIdAndIsDefault(isDefault, userId, oldIsDefault);
    }
}




