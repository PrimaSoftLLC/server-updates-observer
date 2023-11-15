package by.aurorasoft.updatesobserver.util;

import lombok.experimental.UtilityClass;

import java.io.File;

@UtilityClass
public final class FileUtil {

    public static boolean isEmpty(final File file) {
        return file.length() == 0;
    }

}
