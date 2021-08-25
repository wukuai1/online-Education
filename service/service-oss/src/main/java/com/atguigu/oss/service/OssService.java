package com.atguigu.oss.service;

import net.sf.jsqlparser.schema.MultiPartName;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String uploadFileAvatar(MultipartFile file);
}
