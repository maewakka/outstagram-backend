package com.woo.outstagram;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@MapperScan("com.woo.outstagram.mapper")
@EnableConfigurationProperties
@EnableJpaAuditing
public class OutstagramBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutstagramBackendApplication.class, args);
    }

}
