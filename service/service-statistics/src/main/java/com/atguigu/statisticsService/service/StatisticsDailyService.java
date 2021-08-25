package com.atguigu.statisticsService.service;

import com.atguigu.statisticsService.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-08-07
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    void saveCountRegister(String day);

    Map<String, Object> getChartData(String begin, String end, String type);
}
