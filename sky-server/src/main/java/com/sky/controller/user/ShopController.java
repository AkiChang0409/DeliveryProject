package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("user/shop")
@Api(tags = "Shop Management Interface")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;
//    private ValueOperations redisOperations = redisTemplate.opsForValue();

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
