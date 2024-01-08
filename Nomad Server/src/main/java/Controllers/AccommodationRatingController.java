package Controllers;

import DTO.RatingCreationDTO;
import DTO.RatingDTO;
import Services.AccommodationRatingService;
import Services.IService;
import model.AccommodationRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(
        origins = {
                "http://localhost:4200"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })

@RestController
@RequestMapping("/api/accommodation-ratings")
@ComponentScan(basePackageClasses = IService.class)
public class AccommodationRatingController {

    @Autowired
    private AccommodationRatingService accommodationRatingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationRating>> getRatings() {
        Collection<AccommodationRating> accommodationRatings = accommodationRatingService.findAll();
        return new ResponseEntity<Collection<AccommodationRating>>(accommodationRatings, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationRating> getRating(@PathVariable("id") Long id) {
        AccommodationRating rating = accommodationRatingService.findOne(id);

        if (rating == null) {
            return new ResponseEntity<AccommodationRating>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<AccommodationRating>(rating, HttpStatus.OK);
    }

    @GetMapping(value = "/for-accommodation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<RatingDTO>> getRatingsForAccommodation(@PathVariable("id") Long id) {
        Collection<AccommodationRating> accommodationRatings = accommodationRatingService.findRatingsForAccommodation(id);
        return new ResponseEntity<Collection<RatingDTO>>(accommodationRatings.stream().map(this::mapRating).toList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('GUEST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingCreationDTO> createRating(@RequestBody RatingCreationDTO ratingCreationDTO) throws Exception {
        AccommodationRating rating = this.mapRatingDTO(ratingCreationDTO);
        accommodationRatingService.create(rating);
        return new ResponseEntity<RatingCreationDTO>(ratingCreationDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AccommodationRating> deleteRating(@PathVariable("id") Long id) {
        accommodationRatingService.delete(id);
        return new ResponseEntity<AccommodationRating>(HttpStatus.NO_CONTENT);
    }
    public RatingDTO mapRating(AccommodationRating rating){
        RatingDTO dto = new RatingDTO();
        dto.setText(rating.getText());
        dto.setRating(rating.getRating());
        dto.setUserName(rating.getUser().getUsername());
        return dto;
    }
    public AccommodationRating mapRatingDTO(RatingCreationDTO dto){
        AccommodationRating rating = new AccommodationRating();
        rating.setAccommodationId(dto.getRatedId());
        rating.setText(dto.getText());
        rating.setRating(dto.getRating());
        rating.setUserId(dto.getUserId());

        return rating;
    }

}
