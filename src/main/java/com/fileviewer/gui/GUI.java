package com.fileviewer.gui;

import com.fileviewer.controller.Controller;
import com.fileviewer.dto.ChangeViewDTO;
import com.fileviewer.dto.LoadFileDTO;
import com.fileviewer.dto.PageChangeDTO;
import com.fileviewer.gui.progressbar.ProgressBar;
import com.fileviewer.gui.progressbar.ProgressBarFactory;import com.fileviewer.model.Model;
import com.fileviewer.observer.ProgObserver;
import com.fileviewer.observer.ProgObserverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class GUI extends JFrame {
    private static final Logger logger = LogManager.getLogger(GUI.class);

    private final Controller controller;
    private final ProgObserverFactory progObserverFactory;
    private final ProgressBarFactory progressBarFactory;

    private JTextArea textArea;
    private JScrollPane scrollableTextArea;
    private final Container container;

    private final JLabel pageInfoLabel;
    private final JLabel fileSizeLabel;
    private final JLabel fileNameLabel;

    private static enum Page {
        FIRST_PAGE,
        NEXT_PAGE,
        PREV_PAGE
    }

    public GUI(Controller controller, ProgObserverFactory progObserverFactory,
            ProgressBarFactory progressBarFactory) {
        logger.debug("Constructing GUI.");

        // Dependencies.
        this.controller = controller;
        this.progObserverFactory = progObserverFactory;
        this.progressBarFactory = progressBarFactory;

        this.setTitle("File Viewer v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(1000, 800);

        container = this.getContentPane();
        container.setLayout(new BorderLayout());

        Container controlsContainer = new Container();
        controlsContainer.setLayout(new GridLayout(3, 1));

        Container btnContainer = new Container();
        btnContainer.setLayout(new GridLayout(2, 5));

        JButton byteBtn = new JButton("Byte Values");
        byteBtn.addActionListener(e -> changeViewType(DataType.Bytes));

        JButton charBtn = new JButton("Char Values");
        charBtn.addActionListener(e -> changeViewType(DataType.Characters));

        JButton hexBtn = new JButton("Hex Values");
        hexBtn.addActionListener(e -> changeViewType(DataType.Hex));

        JButton UTF8Btn = new JButton("UTF-8 Values");
        UTF8Btn.addActionListener(e -> changeViewType(DataType.UTF8Characters));

        JButton UTF8ByteBtn = new JButton("UTF-8 Codes");
        UTF8ByteBtn.addActionListener(e -> changeViewType(DataType.UTF8Bytes));

        JButton UTF16Btn = new JButton("UTF-16 Values");
        UTF16Btn.addActionListener(e -> changeViewType(DataType.UTF16Characters));

        JButton UTF16ByteBtn = new JButton("UTF-16 Codes");
        UTF16ByteBtn.addActionListener(e -> changeViewType(DataType.UTF16Bytes));

        JButton nxtPageBtn = new JButton("Next Page   ▶");
        nxtPageBtn.addActionListener(e -> displayPage(Page.NEXT_PAGE));

        JButton prevPageBtn = new JButton("◀   Prev. Page");
        prevPageBtn.addActionListener(e -> displayPage(Page.PREV_PAGE));

        JButton firstPageBtn = new JButton("First Page");
        firstPageBtn.addActionListener(e -> displayPage(Page.FIRST_PAGE));

        JButton loadBtn = new JButton("Load File");
        loadBtn.addActionListener(e -> loadFile());

        btnContainer.add(loadBtn);

        btnContainer.add(byteBtn);
        btnContainer.add(charBtn);
        btnContainer.add(hexBtn);
        btnContainer.add(UTF8Btn);
        btnContainer.add(UTF8ByteBtn);
        btnContainer.add(UTF16Btn);
        btnContainer.add(UTF16ByteBtn);

        Container pageControlsContainer = new Container();
        pageControlsContainer.setLayout(new GridLayout(1, 3));

        pageControlsContainer.add(prevPageBtn);
        pageControlsContainer.add(firstPageBtn);
        pageControlsContainer.add(nxtPageBtn);

        pageInfoLabel = new JLabel();
        setPageLabel(1);
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

    public void setPageLabel(int pageNumber) {
        pageInfoLabel.setText("Page number: " + pageNumber);
    }

    public void setFileSizeLabel(int fileSize) {
        fileSizeLabel.setText("File size: " + fileSize + " bytes");
    }

    public void setFileNameLabel(String fileName) {
        fileNameLabel.setText("Filename: " + fileName);
    }

    public void displayError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void changeViewType(DataType type) {
        this.setEnabled(false);

        Thread thread = new Thread(() -> {
                ProgObserver observer = progObserverFactory.getInstance();
                showProgressBar(observer);

                ChangeViewDTO dto = controller.changeViewType(type, observer);

                if (!dto.isErrorOccurred()) {
                    resetTextOutput();
                    appendTextOutput(dto.getData());
                    setPageLabel(dto.getCurrentPage());

                    observer.setIsFinished(true);
                } else {
                    observer.setIsFinished(true);

                    displayError(dto.getErrorMessage());
                }

                this.setEnabled(true);
            });
        thread.start();
    }

    private void loadFile() {
        this.setEnabled(false);
        new Thread(() -> {
            ProgObserver observer = progObserverFactory.getInstance();

            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(this);

            showProgressBar(observer);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                LoadFileDTO dto = controller.loadFile(observer,
                        fileChooser.getSelectedFile());

                if (!dto.isErrorOccurred()) {
                    resetTextOutput();

                    appendTextOutput(dto.getData());
                    setFileNameLabel(dto.getFilename());
                    setPageLabel(dto.getCurrentPage());
                    setFileSizeLabel(dto.getFileSize());

                    observer.setIsFinished(true);
                } else {
                    observer.setIsFinished(true);

                    displayError(dto.getErrorMessage());
                }
            }

            this.setEnabled(true);
        }).start();
    }

    private void displayPage(Enum<Page> page) {
        this.setEnabled(false);
        Thread thread = new Thread(() -> {
            ProgObserver observer = progObserverFactory.getInstance();
            showProgressBar(observer);

            PageChangeDTO dto;
            if (page == Page.FIRST_PAGE)
                dto = controller.showFirstPage(observer);
            else if (page == Page.NEXT_PAGE)
                dto = controller.showNextPage(observer);
            else if (page == Page.PREV_PAGE)
                dto = controller.showPrevPage(observer);
            else
                dto = controller.showFirstPage(observer);

            if (!dto.isErrorOccurred()) {
                resetTextOutput();
                appendTextOutput(dto.getData());
                setPageLabel(dto.getCurrentPage());

                observer.setIsFinished(true);
            } else {
                observer.setIsFinished(true);

                displayMessage(dto.getErrorMessage());
            }

            this.setEnabled(true);
        });
        thread.start();
    }

    private void showProgressBar(ProgObserver observer) {
        Thread thread = new Thread(() -> {
                ProgressBar progressBar = progressBarFactory.getInstance(this, observer);

                while(!observer.isFinished()) {
                    progressBar.setPercentage(observer.getPercentage());

                    try {
                        Thread.sleep(20);
                    } catch (Exception ignored) {}
                }

                logger.debug("Trying to destroy ProgressBar...");
                progressBar.destroyProgressBar();
        });
        thread.start();
    }
}
