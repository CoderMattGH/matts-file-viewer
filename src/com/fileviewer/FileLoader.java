package com.fileviewer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileLoader {
    public FileLoader() {
        System.out.println("Constructing File Loader");
    }

    /**
     * Returns the bytes from the file in an integer array.
     *
     * NOTE: Returns null if an error occurred.
     */
    public int[] loadFile(File file, FileProgObserver fileProgObserver) {
        long fileSize = file.length();

        if (fileSize >= Integer.MAX_VALUE) {
            System.err.println("File size not supported!");

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
                fileProgObserver.setPercentage(percentage);

                //data.add(readByte);
                dataArray[i] = readByte;
                i++;
            }

            bis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        System.out.println("Finished loading file...");

        return dataArray;
    }
}