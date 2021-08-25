package com.atguigu.eduservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Component
@FeignClient("service-order")
public interface OrderClient {

    @GetMapping("/orderService/order/isByCourse/{courseId}/{memberId}")
    public boolean isByCourse(@PathVariable("courseId") String courseId,
                              @PathVariable("memberId") String memberId);

}
