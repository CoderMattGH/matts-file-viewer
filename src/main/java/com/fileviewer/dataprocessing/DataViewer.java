package com.fileviewer.dataprocessing;

import com.fileviewer.observer.FileProgObserver;
import com.fileviewer.gui.GUI;

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

    private static final int APPEND_CHUNK_SIZE = 600;

    private GUI gui;

    public DataViewer() {
        System.out.println("Constructing DataViewer");
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    // Start Index inclusive, End index is exclusive.
    public void displayData(int[] data, FileProgObserver observer, Enum<DataType> type,
            int startByteIndex, int endByteIndex) {
        if (data == null) {
            System.err.println("Data is null!");
            observer.setIsFinished(true);

            return;
        }

        observer.setPercentage(0);
        gui.resetTextOutput();

        StringBuilder str = new StringBuilder();

        int count = 0;

        if (type == DataType.UTF8Bytes || type == DataType.UTF8Characters
                || type == DataType.UTF16Bytes || type == DataType.UTF16Characters) {
            byte[] bytes = getByteArray(data, observer, startByteIndex, endByteIndex - 1);

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            InputStreamReader reader;

            if (type == DataType.UTF8Bytes || type == DataType.UTF8Characters)
                reader = new InputStreamReader(bis, StandardCharsets.UTF_8);
            else
                reader = new InputStreamReader(bis, StandardCharsets.UTF_16);

            int dataByte;
            try {
                while ((dataByte = reader.read()) != -1) {
                    // Is task set to be cancelled?
                    if (observer.isCancelled()) {
                        return;
                    }

                    processChunk(str, type, dataByte, count, observer, bytes.length);
                    count++;
                }

                if (!str.isEmpty())
                    gui.appendTextOutput(str.toString());
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (startByteIndex >= data.length) {
                System.err.println("ERROR: Start Index is bigger than data size.");
                return;
            }

            int endIndex;
            if (endByteIndex > data.length)
                endIndex = data.length;
            else
                endIndex = endByteIndex;

            for (int i = startByteIndex; i < endIndex; i++) {
                int readByte = data[i];

                // Is task set to be cancelled?
                if (observer.isCancelled()) {
                    return;
                }

                processChunk(str, type, readByte, count, observer, endIndex - startByteIndex);
                count++;
            }

            if (!str.isEmpty())
                gui.appendTextOutput(str.toString());
        }

        observer.setPercentage(100);
    }

    // Here endIndex is inclusive.
    private byte[] getByteArray(int[] data, FileProgObserver observer, int startIndex,
            int endIndex) {
        if (endIndex > data.length)
            endIndex = data.length - 1;

        int size = endIndex - startIndex + 1;

        byte[] bytes = new byte[size];

        observer.setPercentage(0);
        double percentage;

        for (int i = 0; i < size; i++) {
            percentage = ((double)i / size) * 100;
            observer.setPercentage(percentage);

            bytes[i] = (byte)data[i + startIndex];
        }

        return bytes;
    }

    private void processChunk(StringBuilder str, Enum<DataType> type, int dataByte, int count,
            FileProgObserver observer, int dataSize) {
        getTypeOutput(type, str, dataByte);

        // Append chars in chunks
        if (count % APPEND_CHUNK_SIZE == 0) {
            double percentage = ((double)count / dataSize) * 100;
            observer.setPercentage(percentage);

            gui.appendTextOutput(str.toString());

            str.setLength(0);
        }

        if (count % 100 == 0) {
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
