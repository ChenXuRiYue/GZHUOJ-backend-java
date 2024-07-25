package com.gzhuoj.problem.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

public interface UploadService {
    Boolean upload(Integer problemNum, List<MultipartFile> testCase, String path, Pattern pattern);
}
