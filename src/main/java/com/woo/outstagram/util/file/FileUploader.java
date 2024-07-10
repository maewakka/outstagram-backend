package com.woo.outstagram.util.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUploader {

    @Value("${path.repositoryPath}")
    private String repositoryPath;

    /**
     * 파일과 저장할 폴더를 지정하면 파일을 저장해주는 메서드
     */
    public void saveFile(String email, Long id, String type, MultipartFile file) {

        Path copyOfLocation = Paths.get(repositoryPath + File.separator +  type + File.separator + email + File.separator + id + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
        Path copyOfDirectory = Paths.get(repositoryPath + File.separator + type + File.separator + email + File.separator + id);
        try {
            Files.createDirectories(copyOfDirectory);
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File {} is saved at Directory [{}]", file.getOriginalFilename(), copyOfLocation);
        } catch (IOException e) {
            log.debug("{}", e.getMessage());
        }
    }

    public void saveFile(String email, String type, MultipartFile file) {

        Path copyOfLocation = Paths.get(repositoryPath + File.separator +  type + File.separator + email + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
        Path copyOfDirectory = Paths.get(repositoryPath + File.separator + type + File.separator + email);
        try {
            Files.createDirectories(copyOfDirectory);
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("File {} is saved at Directory [{}]", file.getOriginalFilename(), copyOfLocation);
        } catch (IOException e) {
            log.debug("{}", e.getMessage());
        }
    }
}
