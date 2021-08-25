package com.atguigu.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    void deleteVodByIdList(List<String> videoIdList);

    String uploadVideoAly(MultipartFile file);
}
