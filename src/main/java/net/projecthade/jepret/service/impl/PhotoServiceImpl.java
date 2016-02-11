package net.projecthade.jepret.service.impl;

import net.projecthade.jepret.domain.Photo;
import net.projecthade.jepret.repository.PhotoRepository;
import net.projecthade.jepret.service.PhotoService;
import net.projecthade.jepret.web.rest.dto.ManagedPhotoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Photo.
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    private final Logger log = LoggerFactory.getLogger(PhotoServiceImpl.class);

    @Inject
    private PhotoRepository photoRepository;

    /**
     * Save a photo.
     *
     * @return the persisted entity
     */
    public Photo save(Photo photo) {
        log.debug("Request to save Photo : {}", photo);
        Photo result = photoRepository.save(photo);
        return result;
    }

    @Override
    public ManagedPhotoDTO create(String name,int rating, double latitude, double longitude ) {
        Photo photo = new Photo(name, rating, latitude, longitude);
        Photo save = photoRepository.save(photo);
        return new ManagedPhotoDTO(save);
    }


    /**
     * get all the photos.
     *
     * @return the list of entities
     */
    public Page<Photo> findAll(Pageable pageable) {
        log.debug("Request to get all Photos");
        Page<Photo> result = photoRepository.findAll(pageable);
        return result;
    }

    /**
     * get one photo by id.
     *
     * @return the entity
     */
    public Photo findOne(String id) {
        log.debug("Request to get Photo : {}", id);
        Photo photo = photoRepository.findOne(id);
        return photo;
    }

    /**
     * delete the  photo by id.
     */
    public void delete(String id) {
        log.debug("Request to delete Photo : {}", id);
        photoRepository.delete(id);
    }

    @Override
    public Optional<Photo> findByName(String name) {
        return photoRepository.findByName(name);
    }

    @Override
    public List<Photo> findByRatingGreaterThan(int rating) {
        return photoRepository.findByRatingGreaterThan(rating);
    }

    @Override
    public List<Photo> findByRatingLessThan(int rating) {
        return photoRepository.findByRatingLessThan(rating);
    }

    @Override
    public GeoResults<Photo> findByLocationNear(double latitude, double longitude, Integer distance) {
        return photoRepository.findByLocationNear(new Point(latitude, longitude), new Distance(distance, Metrics.KILOMETERS));
    }


}
