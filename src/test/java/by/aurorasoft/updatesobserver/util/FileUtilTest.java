package by.aurorasoft.updatesobserver.util;

import by.aurorasoft.updatesobserver.util.FileUtil.FilePath;
import org.junit.Test;

import java.io.File;

import static by.aurorasoft.updatesobserver.util.FileUtil.isEmpty;
import static java.io.File.pathSeparator;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class FileUtilTest {

    @Test
    public void fileShouldBeEmpty() {
        final File givenFile = createFile(0L);

        final boolean actual = isEmpty(givenFile);
        assertTrue(actual);
    }

    @Test
    public void fileShouldNotBeEmpty() {
        final File givenFile = createFile(1L);

        final boolean actual = isEmpty(givenFile);
        assertFalse(actual);
    }

    @Test
    public void fileShouldBeCreated() {
        final String givenDirectoryPath = "directory";
        final String givenFileName = "file";
        final FilePath givenFilePath = createFilePath(givenDirectoryPath, givenFileName);

        final File actual = FileUtil.createFile(givenFilePath);
        final File expected = new File(givenDirectoryPath + pathSeparator + givenFileName);
        assertEquals(expected, actual);
    }

    private static File createFile(final long length) {
        final File file = mock(File.class);
        when(file.length()).thenReturn(length);
        return file;
    }

    @SuppressWarnings("SameParameterValue")
    private static FilePath createFilePath(final String directoryPath, final String fileName) {
        return new FilePath(directoryPath, fileName) {
        };
    }

}
