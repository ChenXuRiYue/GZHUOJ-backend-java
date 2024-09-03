package common.utils;

import cn.hutool.core.lang.Pair;
import common.exception.ClientException;
import common.exception.ServiceException;
import org.gzhuoj.common.sdk.constant.PatternConstant;
import org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.PROBLEM_TEST_CASE_UPLOAD_PATH_ERROR;

public class ZipUtils {
    /**
     * 解压zip文件
     * @param zipFilePath 源路径
     * @param destDir 目标路径
     * @param filenamePattern 正则表达式
     */
    public static boolean decompressZip(String zipFilePath, String destDir, Pattern filenamePattern) throws IOException {
        destDir = underTestCase(destDir);
        Path destDirPath = Paths.get(destDir);
        if (Files.notExists(destDirPath)) {
            Files.createDirectories(destDirPath);
        }
        boolean fail = false;
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(Paths.get(zipFilePath)))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String fileName = Paths.get(zipEntry.getName()).getFileName().toString();
                if (!filenamePattern.matcher(fileName).matches()) {
                    zipEntry = zis.getNextEntry();
                    fail = true;
                    continue;
                }

                Path newFilePath = zipSlipProtect(zipEntry, destDirPath);

                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newFilePath);
                } else {
                    // Create directories for sub directories in zip
                    Path parentDir = newFilePath.getParent();
                    if (Files.notExists(parentDir)) {
                        Files.createDirectories(parentDir);
                    }

                    // Write file content
                    try (FileOutputStream fos = new FileOutputStream(newFilePath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }

                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        return fail;
    }

    private static String underTestCase(String destDir) {
        String testCase = "test_case";
        int idx = destDir.lastIndexOf(testCase);
        if (idx == -1){
            throw new ClientException(PROBLEM_TEST_CASE_UPLOAD_PATH_ERROR);
        }
        return destDir.substring(0, idx + testCase.length());
    }

    private static Path zipSlipProtect(ZipEntry zipEntry, Path destDir) throws IOException {
        Path destDirResolved = destDir.resolve(zipEntry.getName());
        Path normalizePath = destDirResolved.normalize();
        if (!normalizePath.startsWith(destDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }

    public static List<Pair<String, String>> decompressZipToStrings(MultipartFile zipFile) throws Exception {
        List<Pair<String, String>> fileContents = new ArrayList<>();

        Pattern pattern = Pattern.compile(PatternConstant.FILE_IN_ZIP_PATTERN);
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream(), StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // 过滤不合法的文件名
                if (!pattern.matcher(entry.getName()).matches()) {
                    throw new ClientException("ZIP 文件中的文件名不合法: " + entry.getName());
                }

                // 如果不是目录，读取内容并转为字符串
                if (!entry.isDirectory()) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, len);
                    }
                    // 将字节流转换为字符串并添加到列表中
                    String content = outputStream.toString(StandardCharsets.UTF_8);
                    fileContents.add(new Pair<>(entry.getName(), content));
                } else throw new ClientException(BaseErrorCode.PROBLEM_DATA_SET_ZIP_STRUCTURE_ILLEGAL);
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            throw new ServiceException(BaseErrorCode.SERVICE_ERROR);
        }
        return fileContents;
    }

}
