package com.atguigu.statisticsService.controller;


import com.atguigu.commonutils.R;
import com.atguigu.statisticsService.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/statisticsService/sta")
//@CrossOrigin
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    @GetMapping("saveCountRegister/{day}")
    public R saveCountRegister(@PathVariable String day){
        statisticsDailyService.saveCountRegister(day);
        return R.ok();
    }

    @GetMapping("showChart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map<String, Object> map = statisticsDailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }

}

