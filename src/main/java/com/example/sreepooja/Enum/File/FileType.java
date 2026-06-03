package com.example.sreepooja.Enum.File;

public enum FileType {

    POOJAS("poojas"),
    PRIESTS("priests"),
    CATEGORIES("categories"),

    POOJA_SERVICE_THUMBNAILS("pooja-services/thumbnails"),
    POOJA_SERVICE_BANNERS("pooja-services/banners");

    private final String folderName;

    FileType(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}