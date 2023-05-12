    package com.example.braguia.model.trails;

    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.ForeignKey;
    import androidx.room.Index;
    import androidx.room.PrimaryKey;
    import androidx.room.TypeConverters;

    import com.example.braguia.model.trails.converters.EdgeConverter;
    import com.example.braguia.model.trails.converters.TrailTypeConverter;
    import com.google.gson.annotations.SerializedName;

    import java.util.Objects;

    @Entity(
            tableName = "edge",
            foreignKeys = @ForeignKey(
                    entity = Trail.class,
                    parentColumns = "id",
                    childColumns = "edge_trail",
                    onDelete = ForeignKey.CASCADE
            ),
            indices = {
                    @Index(value = {"id"}, unique = true),
                    @Index(value = {"edge_start"}, unique = true),
                    @Index(value = {"edge_end"}, unique = true)
            })
    @TypeConverters({EdgeConverter.class})
    public class Edge{
        @PrimaryKey
        @ColumnInfo(name = "id")
        @SerializedName("id")
        int id;


        @ColumnInfo(name = "edge_start")
        @SerializedName("edge_start")
        EdgeTip edge_start;

        @ColumnInfo(name = "edge_end")
        @SerializedName("edge_end")
        EdgeTip edge_end;

        @ColumnInfo(name = "edge_transport")
        @SerializedName("edge_transport")
        String edge_transport;

        @ColumnInfo(name = "edge_duration")
        @SerializedName("edge_duration")
        int edge_duration;

        @ColumnInfo(name = "edge_desc")
        @SerializedName("edge_desc")
        String edge_desc;

        @ColumnInfo(name = "edge_trail")
        @SerializedName("edge_trail")
        int edge_trail;

        public int getId() {
            return id;
        }

        public EdgeTip getEdge_start() {
            return edge_start;
        }

        public EdgeTip getEdge_end() {
            return edge_end;
        }

        public String getEdge_transport() {
            return edge_transport;
        }

        public int getEdge_duration() {
            return edge_duration;
        }

        public String getEdge_desc() {
            return edge_desc;
        }

        public int getEdge_trail() {
            return edge_trail;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return id == edge.id
                    && edge_duration == edge.edge_duration
                    && edge_trail == edge.edge_trail
                    && Objects.equals(edge_start, edge.edge_start)
                    && Objects.equals(edge_end, edge.edge_end)
                    && Objects.equals(edge_transport, edge.edge_transport)
                    && Objects.equals(edge_desc, edge.edge_desc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, edge_start, edge_end, edge_transport, edge_duration, edge_desc, edge_trail);
        }
    }