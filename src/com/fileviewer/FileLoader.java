package com.fileviewer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class FileLoader {
    public FileLoader() {
        System.out.println("Constructing File Loader");
    }

    public int[] loadFile(File file, FileProgObserver fileProgObserver) {
        int[] dataArray = new int[(int)file.length()];

        try {
            FileInputStream fis = new FileInputStream(file);
            long fileSize = file.length();

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
        }

        System.out.println("Finished loading file...");

        return dataArray;
    }
}