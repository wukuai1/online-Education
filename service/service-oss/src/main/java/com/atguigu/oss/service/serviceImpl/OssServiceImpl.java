package com.atguigu.oss.service.serviceImpl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import net.sf.jsqlparser.schema.MultiPartName;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile file) {

        // yourEndpoint填写Bucket所在地域对应的Endpoint。
        String endpoint = ConstantPropertiesUtils.END_POIND;
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try{
            OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
            InputStream inputStream = file.getInputStream();
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String fileName = file.getOriginalFilename();
            fileName=uuid+fileName;
            String dataPath = new DateTime().toString("yyyy/MM/dd");
            fileName = dataPath+"/"+fileName;
            ossClient.putObject(bucketName,fileName,inputStream);
            ossClient.shutdown();

            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
