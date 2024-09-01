package com.gzhuoj.problem.service.common;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

public interface UploadService {
    void upload(Integer problemNum, List<MultipartFile> testCase, String path, Pattern pattern);
}
