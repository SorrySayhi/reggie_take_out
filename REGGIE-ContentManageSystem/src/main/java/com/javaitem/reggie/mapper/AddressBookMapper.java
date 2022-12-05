package com.javaitem.reggie.mapper;
import org.apache.ibatis.annotations.Param;

import com.javaitem.reggie.domain.AddressBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.javaitem.reggie.domain.AddressBook
 */
public interface AddressBookMapper extends BaseMapper<AddressBook> {

    int updateIsDefaultByUserIdAndIsDefault(@Param("isDefault") Boolean isDefault, @Param("userId") Long userId, @Param("oldIsDefault") Boolean oldIsDefault);

}




