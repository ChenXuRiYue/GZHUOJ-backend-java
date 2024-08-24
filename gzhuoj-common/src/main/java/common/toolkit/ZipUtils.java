package common.toolkit;

import common.convention.errorcode.BaseErrorCode;
import common.exception.ClientException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static common.convention.errorcode.BaseErrorCode.PROBLEM_TEST_CASE_UPLOAD_PATH_ERROR;

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
}
