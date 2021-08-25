package com.atguigu.orderService.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.orderService.client.EduClient;
import com.atguigu.orderService.client.UcenterClient;
import com.atguigu.orderService.entity.Order;
import com.atguigu.orderService.mapper.OrderMapper;
import com.atguigu.orderService.service.OrderService;
import com.atguigu.orderService.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String createOrders(String courseId, String memberId) {


        CourseWebVoOrder courseWebVoOrder = eduClient.getCourseInfoOrder(courseId);
        UcenterMemberOrder ucenterMember = ucenterClient.getUserInfoOrder(memberId);
        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseWebVoOrder.getTitle());
        order.setCourseCover(courseWebVoOrder.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseWebVoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMember.getMobile());
        order.setNickname(ucenterMember.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        order.setGmtCreate(new Date());
        order.setGmtModified(new Date());
        baseMapper.insert(order);

        return order.getOrderNo();

    }
}
