package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService {
    /**
     * 新增菜品，同时插入菜品对应的口味数据
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);

/**
 * 分页查询菜品信息
 * @param dishPageQueryDTO 菜品分页查询条件对象，包含分页参数和查询条件
 * @return PageResult 分页查询结果对象，包含菜品数据列表和分页信息
 */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

/**
 * 批量删除方法
 * 根据提供的ID列表批量删除对应的数据记录
 * @param ids 需要删除的数据ID列表，使用Long类型的List集合存储
 */
    void deleteBatch(List<Long> ids);
}
