package net.projecthade.jepret.service;

import net.projecthade.jepret.domain.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoResults;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Photo.
 */
public interface PhotoService {

    /**
     * Save a photo.
     *
     * @return the persisted entity
     */
    public Photo save(Photo photo);


    public Optional<Photo> update(Photo photo);


    /**
     * get all the photos.
     *
     * @return the list of entities
     */
    public Page<Photo> findAll(Pageable pageable);

    /**
     * get the "id" photo.
     *
     * @return the entity
     */
    public Photo findOne(String id);

    /**
     * delete the "id" photo.
     */
    public void delete(String id);

    public Optional<Photo> findByName(String name);

    public List<Photo> findByRatingGreaterThan(int rating);

    public List<Photo> findByRatingLessThan(int rating);

    public GeoResults<Photo> findByLocationNear(double latitude, double longitude, Integer distance);
}
