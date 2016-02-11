package net.projecthade.jepret.repository;

import net.projecthade.jepret.domain.Photo;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Photo entity.
 */
public interface PhotoRepository extends MongoRepository<Photo, String> {

    Optional<Photo> findByName(String name);

    Optional<Photo> findOneById(String id);

    List<Photo> findByRatingGreaterThan(int rating);

    List<Photo> findByRatingLessThan(int rating);

    GeoResults<Photo> findByLocationNear(Point location, Distance distance);

}
