package net.projecthade.jepret.web.rest;

import net.projecthade.jepret.Application;
import net.projecthade.jepret.domain.Photo;
import net.projecthade.jepret.repository.PhotoRepository;
import net.projecthade.jepret.service.PhotoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PhotoResource REST controller.
 *
 * @see PhotoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoResourceIntTest {

    private static final String DEFAULT_NAME = "AAA";
    private static final String UPDATED_NAME = "BBB";

    private static final double[] LOCATION = new double[2];

    private static final double LATITUDE = 0.0;
    private static final double LONGITUDE = 0.0;


    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    @Inject
    private PhotoRepository photoRepository;

    @Inject
    private PhotoService photoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoMockMvc;

    private Photo photo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoResource photoResource = new PhotoResource();
        ReflectionTestUtils.setField(photoResource, "photoService", photoService);
        this.restPhotoMockMvc = MockMvcBuilders.standaloneSetup(photoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoRepository.deleteAll();
        photo = new Photo();
        photo.setName(DEFAULT_NAME);
        photo.setRating(DEFAULT_RATING);
        LOCATION[0] = LATITUDE;
        LOCATION[1] = LONGITUDE;
        photo.setLocation(LOCATION);
    }

    @Test
    public void createPhoto() throws Exception {
        int databaseSizeBeforeCreate = photoRepository.findAll().size();

        // Create the Photo

        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isCreated());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeCreate + 1);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPhoto.getLocation()).isEqualTo(LOCATION);
        assertThat(testPhoto.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setName(null);

        // Create the Photo, which fails.

        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isBadRequest());

        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoRepository.findAll().size();
        // set the field null
        photo.setLocation(null);

        // Create the Photo, which fails.

        restPhotoMockMvc.perform(post("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isBadRequest());

        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllPhotos() throws Exception {
        // Initialize the database
        photoRepository.save(photo);

        // Get all the photos
        restPhotoMockMvc.perform(get("/api/photos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(photo.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(LOCATION).toString()))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)));
    }

    @Test
    public void getPhoto() throws Exception {
        // Initialize the database
        photoRepository.save(photo);

        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", photo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photo.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(LOCATION))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING));
    }

    @Test
    public void getNonExistingPhoto() throws Exception {
        // Get the photo
        restPhotoMockMvc.perform(get("/api/photos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePhoto() throws Exception {
        // Initialize the database
        photoRepository.save(photo);

        int databaseSizeBeforeUpdate = photoRepository.findAll().size();

        // Update the photo
        photo.setName(UPDATED_NAME);
        LOCATION[0] = LATITUDE;
        LOCATION[1] = LONGITUDE;
        photo.setLocation(LOCATION);
        photo.setRating(UPDATED_RATING);

        restPhotoMockMvc.perform(put("/api/photos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(photo)))
            .andExpect(status().isOk());

        // Validate the Photo in the database
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeUpdate);
        Photo testPhoto = photos.get(photos.size() - 1);
        assertThat(testPhoto.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPhoto.getLocation()).isEqualTo(LOCATION);
        assertThat(testPhoto.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    public void deletePhoto() throws Exception {
        // Initialize the database
        photoRepository.save(photo);

        int databaseSizeBeforeDelete = photoRepository.findAll().size();

        // Get the photo
        restPhotoMockMvc.perform(delete("/api/photos/{id}", photo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Photo> photos = photoRepository.findAll();
        assertThat(photos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
