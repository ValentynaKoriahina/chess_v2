package org.example.chess_v2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class FileUploadDto {
    // getters и setters
    private MultipartFile file;

}
