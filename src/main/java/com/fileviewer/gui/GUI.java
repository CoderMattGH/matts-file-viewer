package com.fileviewer.gui;

import com.fileviewer.controller.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import static com.fileviewer.dataprocessing.DataViewer.DataType;

public class GUI extends JFrame {
    private static final Logger logger = LogManager.getLogger(GUI.class);

    private final Controller controller;

    private JTextArea textArea;
    private JScrollPane scrollableTextArea;
    private final Container container;

    private final JLabel pageInfoLabel;
    private final JLabel fileSizeLabel;
    private final JLabel fileNameLabel;

    public GUI(Controller controller) {
        logger.debug("Constructing GUI.");

        // Dependencies.
        this.controller = controller;

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
        byteBtn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.Bytes)).start();
            });

        JButton charBtn = new JButton("CHAR VALUES");
        charBtn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.Characters)).start();
            });

        JButton hexBtn = new JButton("HEX VALUES");
        hexBtn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.Hex)).start();
            });

        JButton UTF8Btn = new JButton("UTF-8 VALUES");
        UTF8Btn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.UTF8Characters)).start();
            });

        JButton UTF8ByteBtn = new JButton("UTF-8 CODES");
        UTF8ByteBtn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.UTF8Bytes)).start();
            });

        JButton UTF16Btn = new JButton("UTF-16 VALUES");
        UTF16Btn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.UTF16Characters)).start();
            });

        JButton UTF16ByteBtn = new JButton("UTF-16 CODES");
        UTF16ByteBtn.addActionListener(e -> {
                new Thread(() -> controller.changeViewType(DataType.UTF16Bytes)).start();
            });

        JButton nxtPageBtn = new JButton("NEXT PAGE");
        nxtPageBtn.addActionListener(e -> {
                new Thread(controller::showNextPage).start();
            });

        JButton prevPageBtn = new JButton("PREV. PAGE");
        prevPageBtn.addActionListener(e -> {
                new Thread(controller::showPrevPage).start();
            });

        JButton firstPageBtn = new JButton("FIRST PAGE");
        firstPageBtn.addActionListener(e -> {
                new Thread(controller::showFirstPage).start();
            });

        JButton loadBtn = new JButton("LOAD FILE");
        loadBtn.addActionListener(e -> {
                new Thread(controller::loadFile).start();
            });

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

        pageControlsContainer.add(firstPageBtn);
        pageControlsContainer.add(prevPageBtn);
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
}
