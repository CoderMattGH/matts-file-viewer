package com.fileviewer.dataprocessing;

import com.fileviewer.observer.ProgObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DataViewerImpl implements DataViewer {
    private final static Logger logger = LogManager.getLogger(DataViewer.class);

    private static final int CHUNK_SIZE = 600;

    public DataViewerImpl() {
        logger.debug("Constructing DataViewerImpl.");
    }

    /**
     * Returns null if an error occurred or if there was no data to display.
     */
    public String fetchDisplayData(int[] data, ProgObserver observer, Enum<DataType> type,
            int startByteIndex, int endByteIndex) {
        if (data == null) {
            logger.error("Data cannot be null. Returning.");
            observer.setIsFinished(true);

            return null;
        }

        observer.setPercentage(0);

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
                    if (observer.isCancelled()) {
                        return null;
                    }

                    processChunk(str, type, dataByte, count, observer, bytes.length);
                    count++;
                }
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
                logger.error("Start Index cannot be bigger than data size. Returning.");
                return null;
            }

            int endIndex;
            if (endByteIndex > data.length)
                endIndex = data.length;
            else
                endIndex = endByteIndex;

            for (int i = startByteIndex; i < endIndex; i++) {
                int readByte = data[i];

                if (observer.isCancelled()) {
                    return null;
                }

                processChunk(str, type, readByte, count, observer, endIndex - startByteIndex);
                count++;
            }
        }

        observer.setPercentage(100);

        return str.toString();
    }

    // Here endIndex is inclusive.
    private byte[] getByteArray(int[] data, ProgObserver observer, int startIndex,
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
            ProgObserver observer, int dataSize) {
        getTypeOutput(type, str, dataByte);

        if (count % CHUNK_SIZE == 0 && count != 0) {
            double percentage = ((double)count / dataSize) * 100;
            observer.setPercentage(percentage);
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
            logger.error("No Data Type detected when rendering output.");

            throw new RuntimeException("No Data Type detected.");
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
