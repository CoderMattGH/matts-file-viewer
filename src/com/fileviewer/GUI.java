package com.fileviewer;

import com.fileviewer.dataprocessing.DataViewer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class GUI extends JFrame {
    private static final int MAX_BYTES_PER_PAGE = 10000;

    private int startByteIndex = 0;      // Current starting point for reading bytes.
    private DataType currentType = DataType.Characters;

    private final FileLoader fileLoader;
    private final DataViewer dataViewer;

    private int[] lastFileLoadedData;

    private JTextArea textArea;
    private JScrollPane scrollableTextArea;
    private Container container;

    public GUI(FileLoader fileLoader, DataViewer dataViewer) {
        System.out.println("Constructing GUI");

        this.fileLoader = fileLoader;

        this.dataViewer = dataViewer;
        dataViewer.setGui(this);

        this.setTitle("File Viewer v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(1000, 800);

        container = this.getContentPane();
        container.setLayout(new BorderLayout());

        Container btnContainer = new Container();
        btnContainer.setLayout(new GridLayout(2, 5));

        JButton byteBtn = new JButton("BYTE VALUE");
        byteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.Bytes;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton charBtn = new JButton("CHAR VALUE");
        charBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.Characters;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton hexBtn = new JButton("HEX VALUE");
        hexBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.Hex;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton UTF8Btn = new JButton("UTF-8 VALUE");
        UTF8Btn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.UTF8Characters;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton UTF8ByteBtn = new JButton("UTF-8 BYTES");
        UTF8ByteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.UTF8Bytes;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton UTF16Btn = new JButton("UTF-16 VALUE");
        UTF16Btn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.UTF16Characters;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton UTF16ByteBtn = new JButton("UTF-16 BYTES");
        UTF16ByteBtn.addActionListener(e -> {
                Thread thread = new Thread(() -> {
                    startByteIndex = 0;
                    currentType = DataType.UTF16Bytes;
                    displayData(currentType);
                });
                thread.start();
            });

        JButton runGCBtn = new JButton("RUN GC");
        runGCBtn.addActionListener(e -> {
                System.out.println("Running Garbage Collection.");
                System.gc();
            });

        JButton nxtPageBtn = new JButton("NEXT PAGE");
        nxtPageBtn.addActionListener(e -> {
                System.out.println("Fetching next page.");

                startByteIndex = startByteIndex + MAX_BYTES_PER_PAGE;

                Thread thread = new Thread(() -> {
                        displayData(currentType);
                    });
                thread.start();
            });

        JButton prevPageBtn = new JButton("PREV PAGE");
        prevPageBtn.addActionListener(e -> {
                System.out.println("Fetching prev page.");

                int result = startByteIndex - MAX_BYTES_PER_PAGE;

                if (result < 0)
                    startByteIndex = 0;
                else
                    startByteIndex = result;

                Thread thread = new Thread(() -> {
                        displayData(currentType);
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

        btnContainer.add(prevPageBtn);
        btnContainer.add(nxtPageBtn);
        btnContainer.add(button2);

        textArea = new JTextArea();
        textArea.setWrapStyleWord(false);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        scrollableTextArea = new JScrollPane(textArea);
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
            Thread thread = new Thread(() -> {
                    System.out.println("Loading file...");

                    FileProgObserver fileProgObserver = new FileProgObserver();
                    showProgressBar(fileProgObserver);

                    File file = fileChooser.getSelectedFile();

                    lastFileLoadedData = fileLoader.loadFile(file, fileProgObserver);

                    fileProgObserver.setPercentage(0);

                    startByteIndex = 0;

                    dataViewer.displayData(lastFileLoadedData, fileProgObserver, currentType,
                            startByteIndex, startByteIndex + MAX_BYTES_PER_PAGE);
                    fileProgObserver.setIsFinished(true);
                }
            );
            thread.start();
        }
    }

    private void displayData(DataType type) {
        FileProgObserver observer = new FileProgObserver();
        showProgressBar(observer);

        dataViewer.displayData(lastFileLoadedData, observer, currentType, startByteIndex,
                startByteIndex + MAX_BYTES_PER_PAGE);
        observer.setIsFinished(true);
    }

    /**
     * Non-blocking method call.  Creates a thread to show a progress bar and then returns.
     */
    private void showProgressBar(FileProgObserver observer) {
        Thread thread = new Thread(() -> {
                ProgressBar progressBar = new ProgressBar(this, observer);

                GUI.this.setEnabled(false);

                while(!observer.isFinished()) {
                    progressBar.setPercentage(observer.getPercentage());

                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {}
                }

                System.out.println("Trying to destroy progress bar...");
                progressBar.destroyProgressBar();

                GUI.this.setEnabled(true);
            }
        );
        thread.start();
    }

    public void resetTextOutput() {
        container.remove(scrollableTextArea);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);

        scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        container.add(scrollableTextArea, BorderLayout.CENTER);
        container.revalidate();
    }

    public void appendTextOutput(final String text) {
        textArea.append(text);
    }
}
