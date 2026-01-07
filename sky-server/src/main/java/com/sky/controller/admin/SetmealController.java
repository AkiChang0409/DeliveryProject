package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api("Setmeal Management")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

/**
 * 新增套餐接口
 * @PostMapping 表示这是一个HTTP POST请求映射
 * @ApiOperation("new setmeal") 是接口的API文档注释，表示这是一个新增套餐的操作
 * @param setmealDTO 接收前端传来的套餐数据，包含套餐基本信息和关联的菜品信息
 * @return 返回一个统一的Result对象，包含操作结果状态信息
 */
    @PostMapping()
    @ApiOperation("new setmeal")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
    // 使用log记录操作日志，输出新增套餐的信息
        log.info("新增套餐：{}", setmealDTO);
    // 调用业务层方法保存套餐数据及其关联的菜品信息
        setmealService.saveWithDishes(setmealDTO);
    // 返回成功结果，表示套餐添加成功
        return Result.success();
    }

/**
 * 分页查询套餐信息接口
 * @param setmealPageQueryDTO 套餐分页查询条件参数
 * @return 返回分页查询结果，包含套餐数据列表和分页信息
 */
    @GetMapping("/page")
    @ApiOperation("Setmeal pageQuery")  // Swagger API注解，表示这是一个分页查询套餐的接口
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){  // 接收分页查询参数
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);  // 调用服务层方法执行分页查询
        return Result.success(pageResult);  // 返回查询成功结果和数据
    }

    @DeleteMapping
    @ApiOperation("delete set batch")
    public Result delete(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return Result.success();
    }

/**
 * 根据ID获取套餐信息
 * @param id 套餐ID
 * @return 返回套餐的视图对象(SetmealVO)
 */
    @GetMapping("/{id}")    // HTTP GET请求映射，用于处理带有路径变量的请求
    @ApiOperation("get setmeal by id")    // Swagger注解，表示该接口的用途是获取指定ID的套餐
    public Result<SetmealVO> getById(@PathVariable Long id){    // 方法参数通过路径变量获取ID
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);    // 调用服务层方法获取套餐视图对象
        return Result.success(setmealVO);    // 返回成功结果，包含套餐视图对象
    }

    @PutMapping
    @ApiOperation("Edit setmeal")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("Start or stop setmeal")
    public Result startOrStop(@PathVariable Integer status, Long id){
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}
