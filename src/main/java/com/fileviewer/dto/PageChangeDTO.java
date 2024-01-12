package com.fileviewer.dto;

public class PageChangeDTO {
    private String data;
    private int currentPage;

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
}
