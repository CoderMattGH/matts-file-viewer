package com.fileviewer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GUI extends JFrame {
    private final FileLoader fileLoader;

    private int[] lastFileLoadedData;

    private final JTextArea textArea;

    public GUI(FileLoader fileLoader) {
        System.out.println("Constructing GUI");

        this.fileLoader = fileLoader;

        this.setTitle("File Viewer v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(800, 800);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        Container btnContainer = new Container();
        btnContainer.setLayout(new GridLayout(1, 3));

        JButton byteBtn = new JButton("BYTE VALUE");
        byteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    viewDataAsBytes(lastFileLoadedData, observer);
                });
                thread.start();
            });

        JButton charBtn = new JButton("CHAR VALUE");
        charBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    viewDataAsText(lastFileLoadedData, observer);
                });
                thread.start();
            });

        JButton hexBtn = new JButton("HEX VALUE");
        hexBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    viewDataAsHex(lastFileLoadedData, observer);
                });
                thread.start();
            });

        JButton UTF8Btn = new JButton("UTF-8 VALUE");
        UTF8Btn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    viewDataAsUTF8(lastFileLoadedData, observer);
                });
                thread.start();
            });

        JButton UTF8ByteBtn = new JButton("UTF-8 BYTES");
        UTF8ByteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    viewDataAsUTF8ByteValue(lastFileLoadedData, observer);
                });
                thread.start();
            });

        JButton button2 = new JButton("LOAD FILE");
        button2.addActionListener(e -> loadFile());

        btnContainer.add(byteBtn);
        btnContainer.add(charBtn);
        btnContainer.add(hexBtn);
        btnContainer.add(UTF8Btn);
        btnContainer.add(UTF8ByteBtn);
        btnContainer.add(button2);

        textArea = new JTextArea();
        textArea.setText("Nothing here yet.");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        textArea.setEditable(false);

        JScrollPane scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        container.add(btnContainer, BorderLayout.SOUTH);
        container.add(scrollableTextArea, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private void loadFile() {
        final JFileChooser fileChooser = new JFileChooser();

        int returnVal = fileChooser.showOpenDialog(this);

        // If file exists
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final FileProgObserver fileProgObserver = new FileProgObserver();

            Thread t = new Thread(() -> {
                    System.out.println("Loading file...");

                    File file = fileChooser.getSelectedFile();

                    lastFileLoadedData = fileLoader.loadFile(file, fileProgObserver);

                    fileProgObserver.setPercentage(0);

                    viewDataAsText(lastFileLoadedData, fileProgObserver);
                }
            );
            t.start();

            showProgressBar(fileProgObserver);
        }
    }

    /**
     * Non-blocking method call.  Creates a thread to show a progress bar and then returns.
     */
    private void showProgressBar(FileProgObserver observer) {
        Thread thread = new Thread(() -> {
                ProgressBar progressBar = new ProgressBar();

                GUI.this.setEnabled(false);

                while(!observer.isFinished()) {
                    progressBar.setPercentage(observer.getPercentage());

                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {}
                }

                System.out.println("Trying to destroy progress bar...");
                progressBar.destroyProgressBar();

                GUI.this.setEnabled(true);
            }
        );
        thread.start();
    }

    private void viewDataAsBytes(int[] data, FileProgObserver observer) {
        if (data == null) {
            System.out.println("Data is null!");
            observer.setIsFinished(true);

            return;
        }

        resetTextOutput();

        StringBuilder str = new StringBuilder();

        int count = 0;
        for (int readByte : data) {
            double percentage = ((double)count / data.length) * 100;
            observer.setPercentage(percentage);

            str.append(readByte).append(" ");

            count++;
        }

        setTextOutput(str.toString(), observer);

        observer.setIsFinished(true);
    }

    private void viewDataAsText(int[] data, FileProgObserver observer) {
        if (data == null) {
            System.out.println("Data is null!");

            observer.setIsFinished(true);
            return;
        }

        resetTextOutput();

        StringBuilder str = new StringBuilder();
        int dataSize = data.length;
        int count = 0;
        for (Integer readByte : data) {
            double percentage = ((double)count / dataSize) * 100;
            observer.setPercentage(percentage);

            str.append(Character.toString(readByte));

            count++;
        }

        setTextOutput(str.toString(), observer);

        observer.setIsFinished(true);
    }

    private void viewDataAsUTF8(int[] data, FileProgObserver observer) {
        if (data == null) {
            System.out.println("Data is null!");

            observer.setIsFinished(true);
            return;
        }

        resetTextOutput();

        byte[] bytes = getByteArray(data, observer);

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        InputStreamReader reader = new InputStreamReader(bis, StandardCharsets.UTF_8);

        observer.setPercentage(0);

        try {
            StringBuilder str = new StringBuilder();
            int readByte;
            double percentage;
            int count = 0;
            while ((readByte = reader.read()) != -1) {
                str.append(Character.toString(readByte));

                percentage = ((double)count / data.length) * 100;
                observer.setPercentage(percentage);

                count++;
            }

            setTextOutput(str.toString(), observer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                reader.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        observer.setIsFinished(true);
    }

    private void viewDataAsUTF8ByteValue(int[] data, FileProgObserver observer) {
        int dataSize = data.length;

        resetTextOutput();

        byte[] byteArray = getByteArray(data, observer);

        ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
        InputStreamReader inputStreamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);

        StringBuilder str = new StringBuilder();
        double percentage;
        try {
            int byteRead;
            int count = 0;
            while((byteRead = inputStreamReader.read()) != -1) {
                percentage = ((double)count / data.length) * 100;
                observer.setPercentage(percentage);

                str.append(byteRead).append(" ");

                count++;
            }

            setTextOutput(str.toString(), observer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                inputStreamReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        observer.setIsFinished(true);
    }

    private void viewDataAsHex(int[] data, FileProgObserver observer) {
        if (data == null) {
            System.out.println("Data is null!");

            observer.setIsFinished(true);
            return;
        }

        resetTextOutput();

        StringBuilder str = new StringBuilder();
        int dataSize = data.length;
        int count = 0;
        for (Integer readByte : data) {
            double percentage = ((double)count / dataSize) * 100;
            observer.setPercentage(percentage);

            str.append(String.format("%02x ", readByte));

            count++;
        }

        setTextOutput(str.toString(), observer);

        observer.setIsFinished(true);
    }

    /**
     * Simply converts a List of Integers into a byte array using primitive conversion.
     */
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

    private void setTextOutput(String text) {
        textArea.setText(text);
    }

    private void setTextOutput(String text, FileProgObserver observer) {
        System.out.println("Setting text output...");

        textArea.setVisible(false);

        resetTextOutput();

        observer.setPercentage(0);
        double percentage;

        int chunkSize;
        // 0.5 MB or 500000 bytes
        if (text.length() < 50000) {
            chunkSize = 200;
        } else {
            chunkSize = 200;
        }

        System.out.println("ChunkSize: " + chunkSize);

        for (int i = 0; i < text.length(); i = i + chunkSize) {
            // Write in character chunks.
            if (i % chunkSize == 0) {
                percentage = ((double) i / text.length()) * 100;
                observer.setPercentage(percentage);

                int endIndex = i + chunkSize;

                if (endIndex > text.length()) {
                    endIndex = text.length();
                }

                String test = text.substring(i, endIndex);
                appendTextOutput(test);
            }
        }

        System.out.println("Finished setting text output...");
        textArea.setVisible(true);
    }

    private void resetTextOutput() {
        textArea.setText("");
    }

    private void appendTextOutput(String text) {
        textArea.append(text);
    }
}
