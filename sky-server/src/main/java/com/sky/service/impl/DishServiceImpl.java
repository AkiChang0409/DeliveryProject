package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜品服务实现类
 * 使用@Service注解标记为服务层组件，被Spring容器管理
 * 使用@Slf4j注解提供日志支持，简化日志记录操作
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 保存带有风味的菜品信息
     * 该方法用于将菜品数据及其风味信息一并保存到数据库中
     *
     * @param dishDTO 菜品数据传输对象，包含菜品的基本信息和风味数据
     */
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入一条数据
        dishMapper.insert(dish);

        // 获取insert返回的id值
        Long dishId = dish.getId();

        // 向菜品风味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }


/**
 * 分页查询菜品信息
 * @param dishPageQueryDTO 菜品分页查询条件对象，包含页码和每页大小等信息
 * @return PageResult 分页结果对象，包含总记录数和当前页数据列表
 */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO){
    // 使用PageHelper进行分页设置，传入当前页码和每页显示数量
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
    // 执行分页查询，获取分页结果对象
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
    // 返回封装好的分页结果，包含总记录数和当前页数据列表
        return new PageResult(page.getTotal(),page.getResult());
    }
}
