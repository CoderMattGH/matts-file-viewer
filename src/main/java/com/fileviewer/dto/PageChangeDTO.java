package com.fileviewer.dto;

public class PageChangeDTO {
    private String data;
    private int currentPage;
    private boolean errorOccurred = false;
    private String errorMessage;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setErrorOccurred(boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
