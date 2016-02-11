package net.projecthade.jepret.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;

/**
 * A Photo.
 */

@Document(collection = "photo")
public class Photo implements Serializable {

    @Id
    private String id;

    @NotNull
    @Size(min = 3)
    @Field("name")
    private String name;

    @GeoSpatialIndexed
    @NotNull
    @Field("location")
    private double[] location;

    @Field("rating")
    private Integer rating;

    public Photo() {
    }

    public Photo(String name, Integer rating, double latitude, double longitude) {
        this.name = name;
        this.rating = rating;
        this.location = new double[2];
        location[0] = latitude;
        location[1] = longitude;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Photo{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", location=" + Arrays.toString(location) +
            ", rating=" + rating +
            '}';
    }
}
