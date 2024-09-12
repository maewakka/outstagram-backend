package com.woo.outstagram.util.minio;

import com.woo.exception.util.BizException;
import com.woo.outstagram.dto.minio.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;

import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioProperties minioProperties;
    private final MinioClient minioClient;

    public void putObjectToMinio(MultipartFile multipartFile, String location) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .object(location)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("minio_can_not_put");
        }
    }

    public String getUrlFromMinioObject(String route) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioProperties.getBucketName())
                    .object(route)
                    .expiry(2, TimeUnit.HOURS)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("minio_can_not_get");
        }
    }
}
