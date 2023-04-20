package com.example.braguia.model;

public class Media {
    private int id;
    private String mediaFile;
    private String mediaType;
    private int mediaPin;

    public Media(int id, String mediaFile, String mediaType, int mediaPin) {
        this.id = id;
        this.mediaFile = mediaFile;
        this.mediaType = mediaType;
        this.mediaPin = mediaPin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getMediaPin() {
        return mediaPin;
    }

    public void setMediaPin(int mediaPin) {
        this.mediaPin = mediaPin;
    }
}
