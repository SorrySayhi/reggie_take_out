package com.javaitem.reggie.service;

import com.javaitem.reggie.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface CategoryService extends IService<Category> {

    List<Category> selectNameByType(int type);

}
