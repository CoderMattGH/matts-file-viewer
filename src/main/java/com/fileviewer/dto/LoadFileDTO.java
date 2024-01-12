package com.fileviewer.dto;

public class LoadFileDTO extends PageChangeDTO {
    private int fileSize;
    private String filename;

    public LoadFileDTO() {
        super();
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
