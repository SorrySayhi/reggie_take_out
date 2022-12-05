package com.javaitem.reggie.service;

import com.javaitem.reggie.domain.AddressBook;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
public interface AddressBookService extends IService<AddressBook> {
    int updateIsDefaultByUserIdAndIsDefault(Boolean isDefault, Long userId,Boolean oldIsDefault);


}
