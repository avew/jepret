package net.projecthade.jepret.web.rest.dto;

import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Photo.
 */

public class PhotoDTO {

    @Id
    private String id;

    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;

    private Integer rating;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "PhotoDTO{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", rating=" + rating +
            '}';
    }
}
