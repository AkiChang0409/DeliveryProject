package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/shop")
@Api(tags = "Shop Management Interface")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;
//    private ValueOperations redisOperations = redisTemplate.opsForValue();

/**
 * 设置店铺营业状态的接口方法
 * @param status 店铺营业状态，1表示营业中，其他值表示已打烊
 * @return 返回操作结果
 */
    @PutMapping("/{status}")
    @ApiOperation("Set Shop Status")
    public Result setStatus(@PathVariable Integer status){
    // 记录设置店铺状态的日志信息，根据status值显示不同的状态文字
        log.info("设置店铺营业状态为：{}",status == 1?"营业中":"已打烊");
    // 使用Redis模板将店铺状态保存到Redis中，键为"shopStatus"
        redisTemplate.opsForValue().set("shopStatus",status);

        return Result.success();
    }

/**
 * 获取商铺营业状态的接口方法
 * 通过HTTP GET请求访问"/status"路径
 * 使用Swagger注解标记为API操作，说明其功能为获取商铺状态
 *
 * @return 返回一个Result对象，包含商铺状态信息
 */
    @GetMapping("/status")
    @ApiOperation("Get Shop Status")
    public Result<Integer> getStatus(){
    // 从Redis中获取商铺状态值
    // 使用redisTemplate操作Redis，获取键为"shopStatus"的值
        Integer shopStatus =  (Integer) redisTemplate.opsForValue().get("shopStatus");
    // 记录日志，输出获取到的商铺状态信息
        log.info("获取到的商铺营业状态为:{}",shopStatus);
        return Result.success(shopStatus);
    }
}
