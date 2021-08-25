package com.atguigu.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.exception.GuliException;
import com.atguigu.service.VodService;
import com.atguigu.utils.ConstantVodUtils;
import com.atguigu.utils.InitVodClient;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public void deleteVodByIdList(List<String> videoIdList) {


        try{
            DefaultAcsClient client =  InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.END_POIND);
            DeleteVideoRequest request = new DeleteVideoRequest();

            String idList = null;

            for(String id :videoIdList){
                if(id!=null&&id!=""){
                    if(idList==null){
                        idList=id;
                    }else {
                        idList=idList+","+id;
                    }
                }
            }

            if(idList!=null){
                request.setVideoIds(idList);
                client.getAcsResponse(request);
            }

        } catch(Exception e){
            e.printStackTrace();
            throw new GuliException(20001,"删除视频失败");
        }
    }

    @Override
    public String uploadVideoAly(MultipartFile file) {

        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String title = originalFilename.substring(0, originalFilename.lastIndexOf("."));

            UploadStreamRequest request = new UploadStreamRequest(
                    ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.END_POIND,
                    title, originalFilename, inputStream);

            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。
            // 其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            String videoId = response.getVideoId();
            if (!response.isSuccess()) {
                String errorMessage = "阿里云上传错误：" + "code：" + response.getCode() + ", message：" + response.getMessage();
                if(StringUtils.isEmpty(videoId)){
                    throw new GuliException(20001, errorMessage);
                }
            }

            return videoId;
        } catch (IOException e) {
            throw new GuliException(20001, "guli vod 服务上传失败");
        }
    }


}
