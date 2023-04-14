package com.example.braguia.objects;

import java.util.ArrayList;
import java.util.List;

public class Pin {
    private int id;
    private String pinName;
    private String pinDesc;
    private double pinLat;
    private double pinLng;
    private double pinAlt;
    private List<Media> media;
    private List<RelPin> relatedPins;

    // Constructor
    public Pin(int id, String pinName, String pinDesc, double pinLat, double pinLng, double pinAlt) {
        this.id = id;
        this.pinName = pinName;
        this.pinDesc = pinDesc;
        this.pinLat = pinLat;
        this.pinLng = pinLng;
        this.pinAlt = pinAlt;
        this.media = new ArrayList<>();
        this.relatedPins = new ArrayList<>();
    }

    public Pin(int id, String pinName, String pinDesc, double pinLat, double pinLng, double pinAlt, List<Media> media, List<RelPin> pins) {
        this.id = id;
        this.pinName = pinName;
        this.pinDesc = pinDesc;
        this.pinLat = pinLat;
        this.pinLng = pinLng;
        this.pinAlt = pinAlt;
        this.media = media;
        this.relatedPins = pins;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPinName() {
        return pinName;
    }

    public void setPinName(String pinName) {
        this.pinName = pinName;
    }

    public String getPinDesc() {
        return pinDesc;
    }

    public void setPinDesc(String pinDesc) {
        this.pinDesc = pinDesc;
    }

    public double getPinLat() {
        return pinLat;
    }

    public void setPinLat(double pinLat) {
        this.pinLat = pinLat;
    }

    public double getPinLng() {
        return pinLng;
    }

    public void setPinLng(double pinLng) {
        this.pinLng = pinLng;
    }

    public double getPinAlt() {
        return pinAlt;
    }

    public void setPinAlt(double pinAlt) {
        this.pinAlt = pinAlt;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public List<RelPin> getRelatedPins() {
        return relatedPins;
    }

    public void setRelatedPins(List<RelPin> relatedPins) {
        this.relatedPins = relatedPins;
    }

    // Methods for adding media and related pins
    public void addMedia(Media media) {
        this.media.add(media);
    }

    public void addRelatedPin(RelPin relatedPin) {
        this.relatedPins.add(relatedPin);
    }
}

