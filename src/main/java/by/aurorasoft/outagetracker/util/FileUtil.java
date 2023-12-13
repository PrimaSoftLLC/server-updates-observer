package by.aurorasoft.outagetracker.util;

import lombok.*;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Paths;

@UtilityClass
public class FileUtil {

    /**
     * Checks if the given file is empty.
     *
     * @param file The file to check.
     * @return True if the file is empty, false otherwise.
     */
    public static boolean isEmpty(File file) {
        return file.length() == 0;
    }

    /**
     * Creates a file from the given file path information.
     *
     * @param path The path information for the file.
     * @return The created file.
     */
    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createFile(FilePath path) {
        File file = Paths.get(path.getDirectoryPath(), path.getFileName()).toFile();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static abstract class FilePath {
        private String directoryPath;
        private String fileName;
    }
}
