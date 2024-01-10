package com.fileviewer.gui;

import com.fileviewer.dataprocessing.DataViewer;
import com.fileviewer.dataprocessing.FileLoader;
import com.fileviewer.observer.ProgObserver;
import com.fileviewer.observer.ProgObserverFactory;
import com.fileviewer.observer.ProgObserverImpl;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class GUI extends JFrame {
    private static final int MAX_BYTES_PER_PAGE = 10000;

    private int startByteIndex = 0;      // Current start index for reading bytes.
    private DataType currentType = DataType.Characters;

    private final FileLoader fileLoader;
    private final DataViewer dataViewer;
    private final ProgObserverFactory progObserverFactory;

    private int[] lastFileLoadedData;

    private JTextArea textArea;
    private JScrollPane scrollableTextArea;
    private Container container;

    private JLabel pageInfoLabel;
    private JLabel fileSizeLabel;
    private JLabel fileNameLabel;

    public GUI(FileLoader fileLoader, DataViewer dataViewer,
            ProgObserverFactory progObserverFactory) {
        System.out.println("Constructing GUI");

        // Dependencies.
        this.fileLoader = fileLoader;
        this.dataViewer = dataViewer;
        dataViewer.setGUI(this);
        this.progObserverFactory = progObserverFactory;

        this.setTitle("File Viewer v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(1000, 800);

        container = this.getContentPane();
        container.setLayout(new BorderLayout());

        Container controlsContainer = new Container();
        controlsContainer.setLayout(new GridLayout(3, 1));

        Container btnContainer = new Container();
        btnContainer.setLayout(new GridLayout(2, 5));

        JButton byteBtn = new JButton("BYTE VALUES");
        byteBtn.addActionListener(e -> changeViewType(DataType.Bytes));

        JButton charBtn = new JButton("CHAR VALUES");
        charBtn.addActionListener(e -> changeViewType(DataType.Characters));

        JButton hexBtn = new JButton("HEX VALUES");
        hexBtn.addActionListener(e -> changeViewType(DataType.Hex));

        JButton UTF8Btn = new JButton("UTF-8 VALUES");
        UTF8Btn.addActionListener(e -> changeViewType(DataType.UTF8Characters));

        JButton UTF8ByteBtn = new JButton("UTF-8 CODES");
        UTF8ByteBtn.addActionListener(e -> changeViewType(DataType.UTF8Bytes));

        JButton UTF16Btn = new JButton("UTF-16 VALUES");
        UTF16Btn.addActionListener(e -> changeViewType(DataType.UTF16Characters));

        JButton UTF16ByteBtn = new JButton("UTF-16 CODES");
        UTF16ByteBtn.addActionListener(e -> changeViewType(DataType.UTF16Bytes));

        JButton nxtPageBtn = new JButton("NEXT PAGE");
        nxtPageBtn.addActionListener(e -> showNextPage());

        JButton prevPageBtn = new JButton("PREV. PAGE");
        prevPageBtn.addActionListener(e -> showPrevPage());

        JButton firstPageBtn = new JButton("FIRST PAGE");
        firstPageBtn.addActionListener(e -> showFirstPage());

        JButton loadBtn = new JButton("LOAD FILE");
        loadBtn.addActionListener(e -> loadFile());

        btnContainer.add(byteBtn);
        btnContainer.add(charBtn);
        btnContainer.add(hexBtn);
        btnContainer.add(UTF8Btn);
        btnContainer.add(UTF8ByteBtn);
        btnContainer.add(UTF16Btn);
        btnContainer.add(UTF16ByteBtn);

        btnContainer.add(loadBtn);

        Container pageControlsContainer = new Container();
        pageControlsContainer.setLayout(new GridLayout(1, 3));

        pageControlsContainer.add(firstPageBtn);
        pageControlsContainer.add(prevPageBtn);
        pageControlsContainer.add(nxtPageBtn);

        pageInfoLabel = new JLabel();
        setPageLabel(getCurrentPage());
        pageInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        fileSizeLabel = new JLabel();
        setFileSizeLabel(0);
        fileSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        fileNameLabel = new JLabel();
        setFileNameLabel("None");
        fileNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Container infoControlsContainer = new Container();
        infoControlsContainer.setLayout(new GridLayout(1, 2));

        infoControlsContainer.add(pageInfoLabel);
        infoControlsContainer.add(fileNameLabel);
        infoControlsContainer.add(fileSizeLabel);

        controlsContainer.add(btnContainer);
        controlsContainer.add(pageControlsContainer);
        controlsContainer.add(infoControlsContainer);

        textArea = new JTextArea();
        textArea.setWrapStyleWord(false);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        scrollableTextArea = new JScrollPane(textArea);
        scrollableTextArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        container.add(controlsContainer, BorderLayout.SOUTH);
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

                    ProgObserver observer = progObserverFactory.getInstance();
                    showProgressBar(observer);

                    File file = fileChooser.getSelectedFile();

                    int[] tempFileData = fileLoader.loadFile(file, observer);

                    observer.setPercentage(0);

                    // If an error occurred or file was null then return.
                    if (tempFileData == null) {
                        displayError("File size was too large.");
                        observer.setIsFinished(true);

                        return;
                    }

                    lastFileLoadedData = tempFileData;

                    startByteIndex = 0;

                    setPageLabel(getCurrentPage());
                    setFileSizeLabel(lastFileLoadedData.length);
                    setFileNameLabel(file.getName());

                    dataViewer.displayData(lastFileLoadedData, observer, currentType,
                            startByteIndex, startByteIndex + MAX_BYTES_PER_PAGE);
                    observer.setIsFinished(true);
                });
            thread.start();
        }
    }

    private void displayData(DataType type) {
        ProgObserver observer = progObserverFactory.getInstance();
        showProgressBar(observer);

        dataViewer.displayData(lastFileLoadedData, observer, currentType,
                startByteIndex, startByteIndex + MAX_BYTES_PER_PAGE);

        observer.setIsFinished(true);
    }

    /**
     * Non-blocking method call.  Creates a thread to show a progress bar and then returns.
     */
    private void showProgressBar(ProgObserver observer) {
        Thread thread = new Thread(() -> {
                ProgressBar progressBar = new ProgressBarImpl(this, observer);

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

    private int getCurrentPage() {
        if (startByteIndex == 0)
            return 1;
        else
            return (startByteIndex / MAX_BYTES_PER_PAGE) + 1;
    }

    private void setPageLabel(int pageNumber) {
        pageInfoLabel.setText("Page number: " + pageNumber);
    }

    private void setFileSizeLabel(int fileSize) {
        fileSizeLabel.setText("File size: " + fileSize + " bytes");
    }

    private void setFileNameLabel(String fileName) {
        fileNameLabel.setText("Filename: " + fileName);
    }

    private void displayError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void changeViewType(DataType type) {
        Thread thread = new Thread(() -> {
                startByteIndex = 0;
                setPageLabel(getCurrentPage());

                currentType = type;
                displayData(currentType);
            });
        thread.start();
    }

    private void showNextPage() {
        System.out.println("Fetching next page.");

        if (lastFileLoadedData == null)
            return;

        int tempStartIndex = startByteIndex + MAX_BYTES_PER_PAGE;

        if (tempStartIndex >= lastFileLoadedData.length) {
            displayMessage("No more data.");

            return;
        }

        startByteIndex = tempStartIndex;

        setPageLabel(getCurrentPage());

        Thread thread = new Thread(() -> {
            displayData(currentType);
        });
        thread.start();
    }

    private void showPrevPage() {
        System.out.println("Fetching prev page.");

        int result = startByteIndex - MAX_BYTES_PER_PAGE;

        if (result < 0)
            startByteIndex = 0;
        else
            startByteIndex = result;

        setPageLabel(getCurrentPage());

        Thread thread = new Thread(() -> {
            displayData(currentType);
        });
        thread.start();
    }

    private void showFirstPage() {
        System.out.println("Setting first page");

        startByteIndex = 0;

        setPageLabel(getCurrentPage());

        Thread thread = new Thread(() -> {
            displayData(currentType);
        });
        thread.start();
    }
}
