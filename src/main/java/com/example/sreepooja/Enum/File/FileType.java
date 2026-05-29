package com.example.sreepooja.Enum.File;

public enum FileType {

    POOJAS("poojas"),
    PRIESTS("priests"),
    CATEGORIES("categories");

    private final String folderName;

    FileType(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}