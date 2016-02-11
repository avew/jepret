package net.projecthade.jepret.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.projecthade.jepret.domain.Photo;
import net.projecthade.jepret.service.PhotoService;
import net.projecthade.jepret.web.rest.dto.MessageErrorDTO;
import net.projecthade.jepret.web.rest.util.HeaderUtil;
import net.projecthade.jepret.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.GeoResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Photo.
 */
@RestController
@RequestMapping("/api")
public class PhotoResource {

    private final Logger log = LoggerFactory.getLogger(PhotoResource.class);

    @Inject
    private PhotoService photoService;

    /**
     * POST  /photos -> Create a new photo.
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createPhoto(@Valid @RequestBody Photo photo) throws URISyntaxException {
        log.debug("REST request to save Photo : {}", photo);
        if (photoService.findByName(photo.getName()).isPresent()) {
            return new ResponseEntity<Object>(new MessageErrorDTO("Photo name already exists"), HttpStatus.BAD_REQUEST);
        } else {
            Photo result = photoService.save(photo);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        }
    }

    /**
     * PUT  /photos -> Updates an existing photo.
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updatePhoto(@Valid @RequestBody Photo photo) throws URISyntaxException {
        log.debug("REST request to update Photo : {}", photo);
        return photoService.update(photo)
            .map(x -> new ResponseEntity<>(x, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    /**
     * GET  /photos -> get all the photos.
     */
    @RequestMapping(value = "/photos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Photo>> getAllPhotos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Photos");
        Page<Photo> page = photoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/photos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /photos/:id -> get the "id" photo.
     */
    @RequestMapping(value = "/photos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Photo> getPhoto(@PathVariable String id) {
        log.debug("REST request to get Photo : {}", id);
        Photo photo = photoService.findOne(id);
        return Optional.ofNullable(photo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photos/:id -> delete the "id" photo.
     */
    @RequestMapping(value = "/photos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhoto(@PathVariable String id) {
        log.debug("REST request to delete Photo : {}", id);
        photoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photo", id.toString())).build();
    }


    /**
     * GET  /photos/nearby -> get all the photos nearby
     *
     * @param latitude
     * @param longitude
     * @param distance
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/photos/nearby",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getAllPhotosNearby(
        @RequestParam(name = "latitude", defaultValue = "0.0", required = true) double latitude,
        @RequestParam(name = "longitude", defaultValue = "0.0", required = true) double longitude,
        @RequestParam(name = "distance", defaultValue = "1", required = true) Integer distance
//        @RequestParam(name = "page", defaultValue = "1") Integer page,
//        @RequestParam(name = "size", defaultValue = "50") Integer size,
//        @RequestParam(name = "sort", defaultValue = "ASC") String sort,
//        @RequestParam(name = "sort_by", defaultValue = "name") String sort_by
    )
        throws URISyntaxException {
        log.debug("REST request to get a page of Photos nearby");
        List<Photo> collect = photoService.findByLocationNear(latitude, longitude, distance)
            .getContent()
            .parallelStream()
            .map(GeoResult::getContent)
            .collect(Collectors.toList());
        return new ResponseEntity<Object>(collect, HttpStatus.OK);
//        GeoResults<Photo> byLocationNear = photoService.findByLocationNear(latitude, longitude, distance);
//
//        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(sort), sort_by);
//        GeoPage geoPage = new GeoPage(byLocationNear, pageable, byLocationNear.getContent().size());
////        Page<Photo> usersPage = new PageImpl<>(collect, pageable, collect.size());
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(geoPage, "/api/photos/nearby");
//        return new ResponseEntity<>(geoPage.getContent(), headers, HttpStatus.OK);
    }


}
