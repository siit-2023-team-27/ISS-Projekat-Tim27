package Controllers;

import Services.AccommodationService;
import Services.IService;
import model.Accommodation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/accommodations")
@ComponentScan(basePackageClasses = IService.class)
public class AccommodationController {
    @Autowired
    private IService<Accommodation, Long> accommodationService;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Accommodation>> getGreetings() {
        Collection<Accommodation> accommodations = accommodationService.findAll();
        return new ResponseEntity<Collection<Accommodation>>(accommodations, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Accommodation> createGreeting(@RequestBody Accommodation accommodation) throws Exception {
        accommodationService.create(accommodation);
        return new ResponseEntity<Accommodation>(accommodation, HttpStatus.CREATED);
    }

}
