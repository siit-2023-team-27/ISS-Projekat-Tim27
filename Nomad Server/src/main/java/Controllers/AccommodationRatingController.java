package Controllers;

import DTO.AccommodationRatingCreationDTO;
import DTO.AccommodationRatingDTO;
import Services.AccommodationRatingService;
import Services.IService;
import model.Accommodation;
import model.AccommodationComment;
import model.AccommodationRating;
import model.HostRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

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
    public ResponseEntity<Collection<AccommodationRatingDTO>> getRatingsForAccommodation(@PathVariable("id") Long id) {
        Collection<AccommodationRating> accommodationRatings = accommodationRatingService.findRatingsForAccommodation(id);
        return new ResponseEntity<Collection<AccommodationRatingDTO>>(accommodationRatings.stream().map(this::mapRating).toList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('GUEST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationRatingCreationDTO> createRating(@RequestBody AccommodationRatingCreationDTO ratingCreationDTO) throws Exception {
        AccommodationRating rating = this.mapRatingDTO(ratingCreationDTO);
        accommodationRatingService.create(rating);
        return new ResponseEntity<AccommodationRatingCreationDTO>(ratingCreationDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AccommodationRating> deleteRating(@PathVariable("id") Long id) {
        accommodationRatingService.delete(id);
        return new ResponseEntity<AccommodationRating>(HttpStatus.NO_CONTENT);
    }
    public AccommodationRatingDTO mapRating(AccommodationRating rating){
        AccommodationRatingDTO dto = new AccommodationRatingDTO();
        dto.setText(rating.getText());
        dto.setRating(rating.getRating());
        dto.setUserName(rating.getUser().getUsername());
        dto.setId(rating.getId());
        return dto;
    }
    public AccommodationRating mapRatingDTO(AccommodationRatingCreationDTO dto){
        AccommodationRating rating = new AccommodationRating();
        rating.setAccommodationId(dto.getAccommodationId());
        rating.setText(dto.getText());
        rating.setRating(dto.getRating());
        rating.setUserId(dto.getUserId());

        return rating;
    }

}
