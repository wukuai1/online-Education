package com.atguigu.eduservice.client;


import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient("service-vod")
@Component
public interface VodClient {

    @DeleteMapping("/eduvod/video/deleteVodById/{id}")
    public R deleteVodById(@PathVariable("id") String id);

    @DeleteMapping("/eduvod/video/deleteVodByIdList")
    public R deleteVodByIdList(@RequestParam("videoIdList") List<String> videoIdList);
}
