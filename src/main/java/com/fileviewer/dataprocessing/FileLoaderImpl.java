package com.fileviewer.dataprocessing;

import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileLoaderImpl implements FileLoader {
    private static final Logger logger = LogManager.getLogger(FileLoaderImpl.class);

    public FileLoaderImpl() {
        logger.info("Constructing FileLoaderImpl.");
    }

    public int[] loadFile(File file, ProgObserver observer) {
        long fileSize = file.length();

        if (fileSize >= Integer.MAX_VALUE) {
            logger.error("File size too large!  Must be smaller than Integer.MAX_VALUE bytes.");

            return null;
        }

        int[] dataArray = new int[(int)fileSize];

        try {
            FileInputStream fis = new FileInputStream(file);

            BufferedInputStream bis = new BufferedInputStream(fis);

            int readByte;
            int i = 0;
            while((readByte = bis.read()) != -1) {
                double percentage = ((double)i / fileSize) * 100;
                observer.setPercentage(percentage);

                dataArray[i] = readByte;
                i++;
            }

            bis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        logger.info("Finished loading file: " + file.getAbsolutePath());

        return dataArray;
    }
}