package com.example.braguia.objects;

public class Edge {
    private int id;
    private Pin edgeStart;
    private Pin edgeEnd;
    private String edgeTransport;
    private int edgeDuration;
    private String edgeDesc;
    private int edgeTrail;

    public Edge(int id, Pin edgeStart, Pin edgeEnd, String edgeTransport, int edgeDuration, String edgeDesc, int edgeTrail) {
        this.id = id;
        this.edgeStart = edgeStart;
        this.edgeEnd = edgeEnd;
        this.edgeTransport = edgeTransport;
        this.edgeDuration = edgeDuration;
        this.edgeDesc = edgeDesc;
        this.edgeTrail = edgeTrail;
    }

    public int getId() {
        return id;
    }

    public Pin getEdgeStart() {
        return edgeStart;
    }

    public Pin getEdgeEnd() {
        return edgeEnd;
    }

    public String getEdgeTransport() {
        return edgeTransport;
    }

    public int getEdgeDuration() {
        return edgeDuration;
    }

    public String getEdgeDesc() {
        return edgeDesc;
    }

    public int getEdgeTrail() {
        return edgeTrail;
    }
}
