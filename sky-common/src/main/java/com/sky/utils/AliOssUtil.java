package com.sky.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.AccessControlList;
import com.obs.services.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 文件上传（华为云 OBS）
     */
    public String upload(byte[] bytes, String objectName) {

        // 1. 创建 OBS Client
        ObsClient obsClient = new ObsClient(
                accessKeyId,
                accessKeySecret,
                endpoint
        );

        try {
            // 2. 构造上传请求
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(objectName);
            request.setInput(new ByteArrayInputStream(bytes));

            // ⭐⭐ 核心：显式设置对象为公共读
            request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);

            // 3. 上传
            obsClient.putObject(request);

        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 4. 拼接公网访问 URL
        String fileUrl = "https://"
                + bucketName
                + "."
                + endpoint
                + "/"
                + objectName;

        log.info("文件上传到: {}", fileUrl);
        return fileUrl;
    }
}

//package com.sky.utils;
//
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.OSSException;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import java.io.ByteArrayInputStream;
//
//@Data
//@AllArgsConstructor
//@Slf4j
//public class AliOssUtil {
//
//    private String endpoint;
//    private String accessKeyId;
//    private String accessKeySecret;
//    private String bucketName;
//
//    /**
//     * 文件上传
//     *
//     * @param bytes
//     * @param objectName
//     * @return
//     */
//    public String upload(byte[] bytes, String objectName) {
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        try {
//            // 创建PutObject请求。
//            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }
//
//        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
//        StringBuilder stringBuilder = new StringBuilder("https://");
//        stringBuilder
//                .append(bucketName)
//                .append(".")
//                .append(endpoint)
//                .append("/")
//                .append(objectName);
//
//        log.info("文件上传到:{}", stringBuilder.toString());
//
//        return stringBuilder.toString();
//    }
//}
