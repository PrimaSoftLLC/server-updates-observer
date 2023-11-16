package by.aurorasoft.updatesobserver.util;

import lombok.*;
import lombok.experimental.UtilityClass;

import java.io.File;

import static java.io.File.separator;

@UtilityClass
public final class FileUtil {

    public static boolean isEmpty(final File file) {
        return file.length() == 0;
    }

    public static File createFile(final FilePath path) {
        final String directoryPath = path.getDirectoryPath();
        final String fileName = path.getFileName();
        final String filePath = directoryPath + separator + fileName;
        return new File(filePath);
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
