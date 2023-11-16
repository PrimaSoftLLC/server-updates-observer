package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.io.File;

import static java.io.File.pathSeparator;

@UtilityClass
public final class FileUtil {

    public static boolean isEmpty(final File file) {
        return file.length() == 0;
    }

    public static File createFile(final String directoryPath, final String fileName) {
        final String filePath = directoryPath + pathSeparator + fileName;
        return new File(filePath);
    }

}
