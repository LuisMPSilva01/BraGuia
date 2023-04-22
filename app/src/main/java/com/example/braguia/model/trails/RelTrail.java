package com.example.braguia.model.trails;

public class RelTrail {
    private int id;
    private String value;
    private String attrib;
    private int trail;

    public RelTrail(int id, String value, String attrib, int trail) {
        this.id = id;
        this.value = value;
        this.attrib = attrib;
        this.trail = trail;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getAttrib() {
        return attrib;
    }

    public int getTrail() {
        return trail;
    }
}
