package com.woo.outstagram.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioConfig {

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.access-secret}")
    private String secretKey;

    @Value("${minio.url}")
    private String url;

    // minio 의 url 을 세팅하기 위한 postConstruct
    // 환경변수로 url 이 들어오면 해당 url 을 사용하고, 아니면 properties 에 정의 된 값을 사용
    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        try {
            minioClient.ignoreCertCheck(); // ssl 인증서 연결 무시
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return minioClient;
    }
}
