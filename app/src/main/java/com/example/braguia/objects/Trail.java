package com.example.braguia.objects;

import java.util.List;

public class Trail {
    private int id;
    private String trailImg;
    private List<RelTrail> trails;
    private List<Edge> edges;

    public Trail(int id, String trailImg, List<RelTrail> relTrails, List<Edge> edges) {
        this.id = id;
        this.trailImg = trailImg;
        this.trails = relTrails;
        this.edges = edges;
    }

    public int getId() {
        return id;
    }

    public String getTrailImg() {
        return trailImg;
    }

    public List<RelTrail> getRelTrails() {
        return trails;
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
