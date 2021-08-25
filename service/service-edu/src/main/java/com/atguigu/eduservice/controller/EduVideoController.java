package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-07-29
 */
@RestController
@RequestMapping("/eduservice/eduvideo")
//@CrossOrigin
public class EduVideoController {

    @Autowired
    private VodClient vodClient;

    @Autowired
    private EduVideoService eduVideoService;

    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    @DeleteMapping("deleteVideo/{id}")
    public R deleteVideo(@PathVariable String id){
        EduVideo eduVideo = eduVideoService.getById(id);
        String videoSourceId =eduVideo.getVideoSourceId();

        if(!StringUtils.isEmpty(videoSourceId)){
            vodClient.deleteVodById(videoSourceId);
        }

        boolean result = eduVideoService.removeById(id);
        if(result){
            return R.ok();
        }else {
            throw new GuliException(20001,"删除失败");
        }
    }

    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);

        return R.ok();
    }



}

