package com.fileviewer.dataprocessing;

import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FileLoaderImplTest {
    private final static Logger logger = LogManager.getLogger(FileLoaderImplTest.class);

    public FileLoaderImplTest() {
        logger.debug("Constructing FileLoaderImplTest");
    }

    private File getTestFile1() {
        String path = "." + File.separator + "src" + File.separator + "test" + File.separator
                + "resources" + File.separator + "testfile1.txt";

        return new File(path);
    }

    @Test
    public void loadFileTest_ValidFile() throws IOException {
        File testFile = getTestFile1();

        ProgObserver progObserver = Mockito.mock(ProgObserver.class);

        FileLoader fileLoader = new FileLoaderImpl();

        int[] result = fileLoader.loadFile(testFile, progObserver);

        assertEquals(testFile.length(), result.length);

        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(testFile));
        for (int i = 0; i < testFile.length(); i++) {
            assertEquals(fis.read(), result[i]);
        }

        fis.close();
    }

    @Test
    public void loadFileTest_InvalidFileTooLarge() {
        File testFile = Mockito.mock(File.class);
        Mockito.when(testFile.length()).thenReturn(((long)Integer.MAX_VALUE + 1));

        ProgObserver progObserver = Mockito.mock(ProgObserver.class);

        FileLoader fileLoader = new FileLoaderImpl();

        int[] result = fileLoader.loadFile(testFile, progObserver);

        assertNull(result);
    }
}
