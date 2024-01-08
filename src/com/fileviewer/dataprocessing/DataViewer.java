package com.fileviewer.dataprocessing;

import com.fileviewer.FileProgObserver;
import com.fileviewer.GUI;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataViewer {
    public static enum DataType {
        Bytes,
        Hex,
        Characters,
        UTF8Bytes,
        UTF8Characters,
        UTF16Bytes,
        UTF16Characters,
    }

    private static final int APPEND_CHUNK_SIZE = 100;

    private GUI gui;

    public DataViewer() {
        System.out.println("Constructing DataViewer");
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void displayData(int[] data, FileProgObserver observer, Enum<DataType> type) {
        if (data == null) {
            System.err.println("Data is null!");
            observer.setIsFinished(true);

            return;
        }

        observer.setPercentage(0);
        gui.resetTextOutput();

        StringBuilder str = new StringBuilder();

        if (type == DataType.UTF8Bytes || type == DataType.UTF8Characters
                || type == DataType.UTF16Bytes || type == DataType.UTF16Characters) {
            byte[] bytes = getByteArray(data, observer);

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            InputStreamReader reader;

            if (type == DataType.UTF8Bytes || type == DataType.UTF8Characters)
                reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
            else
                reader = new InputStreamReader(bis, StandardCharsets.UTF_16);

            int dataByte;
            int count = 0;
            try {
                while ((dataByte = reader.read()) != -1) {
                    processChunk(str, type, dataByte, count, observer, data.length);
                    count++;
                }

                if (!str.isEmpty())
                    gui.appendTextOutput(str.toString());
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            int count = 0;
            for (int readByte : data) {
                processChunk(str, type, readByte, count, observer, data.length);
                count++;
            }

            if (!str.isEmpty())
                gui.appendTextOutput(str.toString());
        }

        observer.setPercentage(100);
    }

    private byte[] getByteArray(int[] data, FileProgObserver observer) {
        byte[] bytes = new byte[data.length];

        observer.setPercentage(0);
        double percentage;

        for (int i = 0; i < data.length; i++) {
            percentage = ((double)i / data.length) * 100;
            observer.setPercentage(percentage);

            bytes[i] = (byte)data[i];
        }

        return bytes;
    }

    private void processChunk(StringBuilder str, Enum<DataType> type, int dataByte, int count,
            FileProgObserver observer, int dataSize) {
        getTypeOutput(type, str, dataByte);

        // Append in 1000 char chunks
        if (count % APPEND_CHUNK_SIZE == 0) {
            double percentage = ((double)count / dataSize) * 100;
            observer.setPercentage(percentage);

            gui.appendTextOutput(str.toString());
            str.setLength(0);

            sleep();
        }
    }

    private void getTypeOutput(Enum<DataType> type, StringBuilder str, int dataByte) {
        if (type == DataType.Bytes || type == DataType.UTF8Bytes || type == DataType.UTF16Bytes) {
            str.append(dataByte).append(" ");
        } else if (type == DataType.Characters || type == DataType.UTF8Characters
                || type == DataType.UTF16Characters) {
            str.append(Character.toString(dataByte));
        } else if (type == DataType.Hex) {
            str.append(String.format("%02x ", dataByte));
        } else {
            System.err.println("No Data Type detected when rendering output!");
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
