package com.example.braguia.model.TrailMetrics;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.braguia.model.trails.Trail;
import com.example.braguia.model.user.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "trail_metrics", indices = @Index(value = {"metricId"},unique = true),
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "username",
                        childColumns = "username",
                        onDelete = CASCADE)/*,
                @ForeignKey(entity = Trail.class,
                        parentColumns = "id",
                        childColumns = "trail_id",
                        onDelete = CASCADE)*/
        })
public class TrailMetrics {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "metricId")
    int metricId;

    @NonNull
    @ColumnInfo(name = "username", index = true)
    String username;

    @NonNull
    @ColumnInfo(name = "trail_id", index = true)
    int trail_id;

    @ColumnInfo(name = "completedPercentage")
    float completedPercentage;

    @ColumnInfo(name = "timeTaken")
    float timeTaken;

    @ColumnInfo(name = "vizitedPins")
    String vizitedPins;

    public TrailMetrics(@NonNull String username, int trail_id, float completedPercentage, float timeTaken, List<Integer> visitedPinsList) {
        this.username = username;
        this.trail_id = trail_id;
        this.completedPercentage = completedPercentage;
        this.timeTaken = timeTaken;
        this.vizitedPins = visitedPinsList.stream().map(String::valueOf).collect(Collectors.joining(";"));
    }

    public TrailMetrics(@NonNull String username, int trail_id, float completedPercentage, float timeTaken, String vizitedPins) {
        this.username = username;
        this.trail_id = trail_id;
        this.completedPercentage = completedPercentage;
        this.timeTaken = timeTaken;
        this.vizitedPins = vizitedPins;
    }

    public static String formatPinList(List<Integer> pinIds){
        return pinIds.stream().map(String::valueOf).collect(Collectors.joining(";"));
    }

    public List<Integer> getPinIdList(){
        List<Integer> pinIds = new ArrayList<>();
        String[] pinArray = this.vizitedPins.split(";");
        for(String pinString : pinArray){
            if(pinString!=""){
                pinIds.add(Integer.parseInt(pinString));
            }
        }
        return pinIds;
    }
    @NonNull
    public String getUsername() {
        return username;
    }

    public int getTrail_id() {
        return trail_id;
    }

    public float getCompletedPercentage() {
        return completedPercentage;
    }

    public float getTimeTaken() {
        return timeTaken;
    }

    public String getVizitedPins() {
        return vizitedPins;
    }

    public int getMetricId() {
        return metricId;
    }
}
