package com.atguigu.statisticsService.schedule;


import com.atguigu.statisticsService.client.UcenterMemberClient;
import com.atguigu.statisticsService.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;

public class ScheduledTask {

    @Autowired
    private UcenterMemberClient userClient;

    public void task2(){
        String day = DateUtil.formatDate(DateUtil.addDays(new Date(),-1));
        userClient.getCountRegister(day);
    }
}
