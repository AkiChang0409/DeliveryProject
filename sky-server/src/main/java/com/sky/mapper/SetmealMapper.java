package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from sky_take_out.setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

/**
 * 批量插入套餐菜品关联数据
 * 使用自动填充功能，在插入数据时自动填充操作类型为INSERT
 *
 * @param setmealDishes 套餐菜品关联数据列表，包含需要批量插入的多个套餐菜品关联信息
 */
    void insertBatch(List<SetmealDish> setmealDishes);

/**
 * 插入套餐数据的方法
 * 使用了自动填充注解@AutoFill，当操作类型为INSERT时自动填充相关字段
 *
 * @param setmeal 套餐对象，包含需要插入的套餐信息
 */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    @Select("select * from sky_take_out.setmeal where id = #{id}")
    Setmeal getById(Long id);

    @Delete("delete from sky_take_out.setmeal where id = #{setmealId}")
    void deleteById(Long setmealId);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
}
