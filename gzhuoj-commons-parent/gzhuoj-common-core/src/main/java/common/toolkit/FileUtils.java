package common.toolkit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static boolean isDirectoryExists(String path){
        Path directoryPath = Paths.get(path);
        return Files.exists(directoryPath) && Files.isDirectory(directoryPath);
    }
}
