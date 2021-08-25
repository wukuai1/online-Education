package com.atguigu.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.service.VodService;
import com.atguigu.utils.ConstantVodUtils;
import com.atguigu.utils.InitVodClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
//@CrossOrigin
public class VodController {

    @Autowired
    private VodService vodService;



    @PostMapping("uploadAlyiVideo")
    public R uploadAlyiVideo(MultipartFile file){

        String videoId = vodService.uploadVideoAly(file);

        return R.ok().data("videoId",videoId);
    }

    @DeleteMapping("deleteVodById/{id}")
    public R deleteVodById(@PathVariable String id){
        try{
           DefaultAcsClient client =  InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.END_POIND);
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
            return R.ok();
        } catch(Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败失败");
        }

    }

    @DeleteMapping("deleteVodByIdList")
    public R deleteVodByIdList(@RequestParam("videoIdList") List<String> videoIdList){

        if(videoIdList!=null){
            vodService.deleteVodByIdList(videoIdList);
            return R.ok();
        }else {
            return R.ok().message("视频id为空");
        }

    }
    @GetMapping("getPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId) throws Exception {
        //获取阿里云存储相关常量
        String accessKeyId = ConstantVodUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantVodUtils.END_POIND;
        //初始化
        DefaultAcsClient client = InitVodClient.initVodClient(accessKeyId, accessKeySecret);
        //请求
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);

        //响应
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);

        //得到播放凭证
        String playAuth = response.getPlayAuth();

        System.out.println(playAuth);
        //返回结果
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
    }


}

