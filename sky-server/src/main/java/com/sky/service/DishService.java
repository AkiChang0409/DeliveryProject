package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * 菜品服务接口
 */
public interface DishService {
    /**
     * 新增菜品，同时插入菜品对应的口味数据
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);
}
