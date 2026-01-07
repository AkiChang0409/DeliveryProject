package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    @Autowired
    private SetmealDishMapper setmealDishMapper;


/**
 * 根据ID查询菜品信息，并包含口味数据
 * @param id 菜品的ID
 * @return DishVO 菜品视图对象，包含菜品基本信息和口味信息
 */
    public DishVO getByIdWithFlavor(Long id) {
        // 根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 将数据封装到VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * 保存带有风味的菜品信息
     * 该方法用于将菜品数据及其风味信息一并保存到数据库中
     *
     * @param dishDTO 菜品数据传输对象，包含菜品的基本信息和风味数据
     */
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

    public void deleteBatch(List<Long> ids){
        // 判断菜品是否能够删除--是否存在起售中的菜品
        for(Long id : ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                // 起售中，不允许删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断当前菜品能否删除--套餐是否被关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds != null && !setmealIds.isEmpty()){
            // 当前菜品被套餐关联了，不允许删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 删除菜品数据
        dishMapper.deleteBatch(ids);
        // 删除菜品口味数据
        dishFlavorMapper.deleteBatch(ids);
    }


    public void updateWithFlavor(DishDTO dishDTO) {
        // 修改菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        //口味数据处理
        List<DishFlavor> flavors = dishDTO.getFlavors();
        List<Long> ids = Collections.singletonList(dishDTO.getId());
        // 删除原有的口味数据
        dishFlavorMapper.deleteBatch(ids);

        if (flavors != null && !flavors.isEmpty()) {
            // 插入新的口味数据
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
