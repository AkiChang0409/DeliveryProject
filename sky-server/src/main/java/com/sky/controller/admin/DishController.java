package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags="Dish Interface")
@Slf4j

public class DishController {

    @Autowired
    private DishService dishService;

/**
 * 创建菜品接口
 * @param dishDTO 菜品数据传输对象，包含菜品的基本信息和口味信息
 * @return 返回操作结果，成功时返回成功状态
 */
    @PostMapping
    @ApiOperation("Create Dish")
    public Result save(@RequestBody DishDTO dishDTO){
    // 打印日志，记录新增菜品的信息
        log.info("新增菜品：{}",dishDTO);
    // 调用业务层方法，保存菜品及其口味信息
        dishService.saveWithFlavor(dishDTO);
    // 返回操作成功的结果
        return Result.success();
    }

/**
 * 菜品分页查询接口
 * @param dishPageQueryDTO 菜品分页查询条件数据传输对象
 * @return 返回分页查询结果，包含菜品信息列表及分页数据
 */
    @GetMapping("/page")    // HTTP GET请求映射，指定访问路径为"/page"
    @ApiOperation("Dish Page Query")  // Swagger API注解，描述接口功能为"菜品分页查询"
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);  // 记录日志，输出分页查询条件
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);  // 调用服务层方法执行分页查询
        return Result.success(pageResult);  // 返回成功响应，封装分页查询结果
    }
}
