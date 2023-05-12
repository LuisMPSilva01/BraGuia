package com.example.braguia.model;

import com.example.braguia.model.TrailMetrics.TrailMetrics;
import com.example.braguia.model.trails.EdgeTip;
import com.example.braguia.model.trails.Trail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Trip {
    private long timeStart;
    private long distance;
    private int trailId;
    private List<EdgeTip> percorridos;
    private List<EdgeTip> previstos;

    public Trip(Trail trail) {
        this.timeStart = new Date().getTime();
        this.distance = 0;
        this.trailId=trail.getId();
        this.percorridos= new ArrayList<>();
        this.previstos=trail.getRoute();
    }

    public TrailMetrics finish(String username) {
        float percentageCompletion = (float) percorridos.size()/(previstos.size()+percorridos.size());
        float timeTaken = (float) (new Date().getTime() - timeStart)/1000; //returns in seconds
        return new TrailMetrics(username,trailId,percentageCompletion,timeTaken,percorridos.stream().map(EdgeTip::getId).collect(Collectors.toList()));
    }
}
