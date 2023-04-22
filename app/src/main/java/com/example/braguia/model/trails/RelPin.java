package com.example.braguia.model.trails;

public class RelPin {
    private int id;
    private String value;
    private String attrib;
    private int pin;

    // Constructors
    public RelPin() {}

    public RelPin(int id, String value, String attrib, int pin) {
        this.id = id;
        this.value = value;
        this.attrib = attrib;
        this.pin = pin;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttrib() {
        return attrib;
    }

    public void setAttrib(String attrib) {
        this.attrib = attrib;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
