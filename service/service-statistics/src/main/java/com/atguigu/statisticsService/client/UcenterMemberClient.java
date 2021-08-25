package com.atguigu.statisticsService.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterMemberClient {

    @GetMapping("/educenter/member/getCountRegister/{day}")
    public int getCountRegister(@PathVariable("day") String day);

}
