package com.fileviewer;

import com.fileviewer.dataprocessing.DataViewer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class GUI extends JFrame {
    private final FileLoader fileLoader;
    private final DataViewer dataViewer;

    private int[] lastFileLoadedData;

    private JTextArea textArea;

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
        btnContainer.setLayout(new GridLayout(2, 5));

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

        JButton runGCBtn = new JButton("RUN GC");
        runGCBtn.addActionListener(e -> {
                System.out.println("Running Garbage Collection.");
                System.gc();
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

        btnContainer.add(runGCBtn);
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
            Thread t = new Thread(() -> {
                    System.out.println("Loading file...");

                    FileProgObserver fileProgObserver = new FileProgObserver();
                    showProgressBar(fileProgObserver);

                    File file = fileChooser.getSelectedFile();

                    lastFileLoadedData = fileLoader.loadFile(file, fileProgObserver);

                    fileProgObserver.setPercentage(0);

                    dataViewer.displayData(lastFileLoadedData, fileProgObserver, DataType.Characters);
                    fileProgObserver.setIsFinished(true);
                }
            );
            t.start();
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
                        Thread.sleep(200);
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

    public void resetTextOutput() {
        textArea.setText(null);
        textArea.revalidate();
    }

    public void appendTextOutput(final String text) {
        textArea.append(text);
    }
}
