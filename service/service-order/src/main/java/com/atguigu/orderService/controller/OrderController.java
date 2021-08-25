package com.atguigu.orderService.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.jwt.JwtUtils;
import com.atguigu.orderService.entity.Order;
import com.atguigu.orderService.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/orderService/order")
//@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        String orderNo = orderService.createOrders(courseId, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data("orderId",orderNo);
    }

    @GetMapping("getOrderInfo/{orderNo}")
    public R getOrderInfo(@PathVariable String orderNo){

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no",orderNo);
        Order order = orderService.getOne(queryWrapper);
        return R.ok().data("item",order);
    }

    @GetMapping("isByCourse/{courseId}/{memberId}")
    public boolean isByCourse(@PathVariable String courseId,
                              @PathVariable String memberId){

        QueryWrapper queryWrapper  = new QueryWrapper();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.eq("member_id",memberId);
        queryWrapper.eq("status",1);

       return  orderService.count(queryWrapper)>0;
    }
}

