package com.woo.outstagram.dto.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Data
public class UploadPostRequestDto {

    @NotNull(message = "사진은 필수 항목입니다.")
    @Size(min = 1)
    private List<MultipartFile> file;

    private String content;
}
