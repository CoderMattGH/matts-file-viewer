package com.fileviewer;

import com.fileviewer.dataprocessing.DataViewer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class GUI extends JFrame {
    private final FileLoader fileLoader;
    private final DataViewer dataViewer;

    private int[] lastFileLoadedData;

    private final JTextArea textArea;

    private static final int APPEND_CHUNK_SIZE = 1000;

    public GUI(FileLoader fileLoader, DataViewer dataViewer) {
        System.out.println("Constructing GUI");

        this.fileLoader = fileLoader;

        this.dataViewer = dataViewer;
        dataViewer.setGui(this);

        this.setTitle("File Viewer v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(1000, 800);

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        Container btnContainer = new Container();
        btnContainer.setLayout(new GridLayout(2, 4));

        JButton byteBtn = new JButton("BYTE VALUE");
        byteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.Bytes);
                    observer.setIsFinished(true);
                });
                thread.start();
            });

        JButton charBtn = new JButton("CHAR VALUE");
        charBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.Characters);
                    observer.setIsFinished(true);
                });
                thread.start();
            });

        JButton hexBtn = new JButton("HEX VALUE");
        hexBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.Hex);
                    observer.setIsFinished(true);
                });
                thread.start();
            });

        JButton UTF8Btn = new JButton("UTF-8 VALUE");
        UTF8Btn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.UTF8Characters);
                    observer.setIsFinished(true);
                });
                thread.start();
            });

        JButton UTF8ByteBtn = new JButton("UTF-8 BYTES");
        UTF8ByteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.UTF8Bytes);
                    observer.setIsFinished(true);
                });
                thread.start();
            });

        JButton UTF16Btn = new JButton("UTF-16 VALUE");
        UTF16Btn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.UTF16Characters);
                    observer.setIsFinished(true);
                });
                thread.start();
            });

        JButton UTF16ByteBtn = new JButton("UTF-16 BYTES");
        UTF16ByteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    FileProgObserver observer = new FileProgObserver();
                    showProgressBar(observer);

                    dataViewer.displayData(lastFileLoadedData, observer, DataType.UTF16Bytes);
                    observer.setIsFinished(true);
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
        btnContainer.add(UTF16Btn);
        btnContainer.add(UTF16ByteBtn);
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

                    dataViewer.displayData(lastFileLoadedData, fileProgObserver, DataType.Characters);
                    fileProgObserver.setIsFinished(true);
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
                        Thread.sleep(100);
                    } catch (Exception e) {}
                }

                System.out.println("Trying to destroy progress bar...");
                progressBar.destroyProgressBar();

                GUI.this.setEnabled(true);
            }
        );
        thread.start();
    }

    public void setTextOutput(String text) {
        textArea.setText(text);
    }

    /**
     * Slow method.  Only use as last resort.
     */
    public void setTextOutput(String text, FileProgObserver observer) {
        System.out.println("Setting text output...");

        textArea.setVisible(false);

        resetTextOutput();

        observer.setPercentage(0);
        double percentage;

        int chunkSize;
        if (text.length() < 50000) {
            chunkSize = 100;
        } else {
            chunkSize = 100;
        }

        System.out.println("ChunkSize: " + chunkSize);

        for (int i = 0; i < text.length(); i++) {
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

            if (i % (chunkSize * 10) == 0) {
                try {
                    System.out.println("Sleeping...");
                    Thread.sleep(5);
                } catch (Exception e) {}
            }
        }

        System.out.println("Finished setting text output...");
        textArea.setVisible(true);
    }

    public void resetTextOutput() {
        textArea.setText("");
    }

    public void appendTextOutput(final String text) {
        textArea.append(text);
    }
}
