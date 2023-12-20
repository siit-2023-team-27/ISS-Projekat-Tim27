package Controllers;

import Services.HostRatingService;
import Services.IService;
import model.Accommodation;
import model.AccommodationRating;
import model.HostComment;
import model.HostRating;
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
@RequestMapping("/api/host-ratings")
@ComponentScan(basePackageClasses = IService.class)
public class HostRatingController {

    @Autowired
    private IService<HostRating, Long> hostRatingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<HostRating>> getRatings() {
        Collection<HostRating> ratings = hostRatingService.findAll();
        return new ResponseEntity<Collection<HostRating>>(ratings, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HostRating> getRating(@PathVariable("id") Long id) {
        HostRating rating = hostRatingService.findOne(id);

        if (rating == null) {
            return new ResponseEntity<HostRating>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<HostRating>(rating, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('GUEST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HostRating> createRating(@RequestBody HostRating rating) throws Exception {
        hostRatingService.create(rating);
        return new ResponseEntity<HostRating>(rating, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAuthority('GUEST') or hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HostRating> deleteRating(@PathVariable("id") Long id) {
        hostRatingService.delete(id);
        return new ResponseEntity<HostRating>(HttpStatus.NO_CONTENT);
    }
}
