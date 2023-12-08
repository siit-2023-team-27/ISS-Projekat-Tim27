package Controllers;

import DTO.AccommodationDTO;
import Services.AccommodationService;
import Services.IService;
import Services.ReservationService;
import model.Accommodation;
import model.Amenity;
import model.DateRange;
import model.Reservation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
@RequestMapping("/api/accommodations")
@ComponentScan(basePackageClasses = IService.class)
public class AccommodationController {
    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private ModelMapper modelMapper;

    //@PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationDTO>> getAccommodations() {
        Collection<Accommodation> accommodations = accommodationService.findAll();
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/unverified", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Collection<AccommodationDTO>> getUnverifiedAccommodations() {
        Collection<Accommodation> accommodations = accommodationService.getUnverifiedAccommodations();
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Accommodation>> searchccommodations(@RequestParam(required = true) String city,
                                                                       @RequestParam(required = true) LocalDate from,
                                                                       @RequestParam(required = true) LocalDate to,
                                                                       @RequestParam(required = true) int peopleNum) {

        Collection<Accommodation> accommodations = accommodationService.search(city, new DateRange(from, to), peopleNum);

        return new ResponseEntity<Collection<Accommodation>>(accommodations, HttpStatus.OK);
    }
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AccommodationDTO>> filterAccommodations(@RequestParam(required = false) String name,
        @RequestParam(required = false) Integer minimumGuests, @RequestParam(required = false) Integer maximumGuests,
        @RequestParam(required = false) Date minimumDate, @RequestParam(required = false) Date maximumDate,
        @RequestParam(required = false) Double minimumPrice, @RequestParam(required = false) Double maximumPrice,
        @RequestParam(required = false) List<String> amenity) {
        Collection<Accommodation> accommodations = accommodationService.findAll();
        Collection<AccommodationDTO> accommodationDTOS = accommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<AccommodationDTO>>(accommodationDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> getAccommodation(@PathVariable("id") Long id) {
        Accommodation accommodation = accommodationService.findOne(id);

        if (accommodation == null) {
            return new ResponseEntity<AccommodationDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<AccommodationDTO>(this.convertToDto(accommodation), HttpStatus.OK);
    }

    @GetMapping("/{accommodationId}/amenities")
    public ResponseEntity<Collection<Amenity>> getAmenitiesForAccommodation(@PathVariable long accommodationId) {
        return new ResponseEntity<Collection<Amenity>>(accommodationService.getAllAmenitiesForAccommodation(accommodationId), HttpStatus.OK);
    }
    @PostMapping("/{accommodationId}/amenities")
    public ResponseEntity<Amenity> addAmenityToAccommodation(@PathVariable long accommodationId, @RequestBody Amenity newAmenity) {
        accommodationService.addAmenityToAccommodation(accommodationId, newAmenity);
        return new ResponseEntity<Amenity>(newAmenity, HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> createAccommodation(@RequestBody AccommodationDTO accommodationDTO) throws Exception {
        Accommodation accommodation = this.convertToEntity(accommodationDTO);
        accommodationService.create(accommodation);
        return new ResponseEntity<AccommodationDTO>(accommodationDTO, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<AccommodationDTO> deleteAccommodation(@PathVariable("id") Long id) {
        accommodationService.delete(id);
        return new ResponseEntity<AccommodationDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> updateAccommodation(@RequestBody AccommodationDTO accommodationDTO, @PathVariable Long id)
            throws Exception {
        Accommodation accommodationForUpdate = accommodationService.findOne(id);
        Accommodation updatedAccommodation = this.convertToEntity(accommodationDTO);
        accommodationForUpdate.copyValues(updatedAccommodation);

        accommodationService.update(accommodationForUpdate);

        if (updatedAccommodation == null) {
            return new ResponseEntity<AccommodationDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<AccommodationDTO>(accommodationDTO, HttpStatus.OK);
    }
    @PutMapping(value = "verify/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> updateAccommodation(@PathVariable Long id)
            throws Exception {
        Accommodation accommodationForUpdate = accommodationService.findOne(id);
        accommodationForUpdate.setVerified(true);
        accommodationService.update(accommodationForUpdate);



        return new ResponseEntity<AccommodationDTO>(convertToDto(accommodationForUpdate), HttpStatus.OK);
    }
    @PutMapping(value = "/favourite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> favouriteAccommodation(@RequestParam() Long userID, @RequestParam() Long accommodationID) {
        Accommodation accommodation = this.accommodationService.findOne(accommodationID);
        if(accommodation == null){
            return new ResponseEntity<AccommodationDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AccommodationDTO>(this.convertToDto(accommodation),HttpStatus.OK);
    }
    @PutMapping(value = "/un-favourite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccommodationDTO> unFavouriteAccommodation(@RequestParam() Long userID, @RequestParam() Long accommodationID) {
        Accommodation accommodation = this.accommodationService.findOne(accommodationID);
        if(accommodation == null){
            return new ResponseEntity<AccommodationDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<AccommodationDTO>(this.convertToDto(accommodation),HttpStatus.OK);
    }
    private AccommodationDTO convertToDto(Accommodation accommodation) {
        AccommodationDTO accommodationDTO = modelMapper.map(accommodation, AccommodationDTO.class);

        return accommodationDTO;
    }
    private Accommodation convertToEntity(AccommodationDTO accommodationDTO) {
        return modelMapper.map(accommodationDTO, Accommodation.class);
    }
}
