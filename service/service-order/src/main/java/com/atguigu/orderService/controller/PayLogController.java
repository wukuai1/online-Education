package com.atguigu.orderService.controller;


import com.atguigu.commonutils.R;
import com.atguigu.orderService.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/orderService/payLog")
//@CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map = payLogService.createNative(orderNo);
        return R.ok().data(map);
    }

    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map = payLogService.queryPayStatus(orderNo);
        if(map==null){
            return R.error().message("支付出错了");
        }

        if(map.get("trade_state").equals("SUCCESS")){
            payLogService.updateOrdersStatus(map);
            return  R.ok().message("支付成功");
        }

        return R.ok().code(25000).message("支付中");
    }
}

