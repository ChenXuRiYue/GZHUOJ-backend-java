package com.gzhuoj.contest.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

public interface UploadService {
    Boolean upload(Integer contestNum, List<MultipartFile> description, String problemUploadPath, Pattern compile);
}
