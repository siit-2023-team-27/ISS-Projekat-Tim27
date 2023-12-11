package Controllers;


import DTO.AccommodationDTO;
import DTO.ReservationDTO;
import Services.AccommodationService;
import Services.IService;
import Services.ReservationService;
import Services.UserService;
import model.Accommodation;
import model.DateRange;
import model.Guest;
import model.Reservation;
import model.enums.ReservationStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
@RequestMapping("/api/reservations")
@ComponentScan (basePackageClasses = IService.class)
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> getReservations() {
        Collection<Reservation> reservations = reservationService.findAll();
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }

    @GetMapping (value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findOne(id);
        if(reservation == null) { return new ResponseEntity<ReservationDTO>(HttpStatus.NOT_FOUND); }

        return new ResponseEntity<ReservationDTO>(this.convertToDto(reservation), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> createReservation (@RequestBody ReservationDTO reservationDTO) {
        Reservation newReservation = this.convertToEntity(reservationDTO);
        if(reservationService.reserve(newReservation)){
            return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.CREATED);
        }
        return new ResponseEntity<ReservationDTO>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ReservationDTO> deteteReservation(@PathVariable("id") Long id) {
        Reservation reservation = reservationService.findOne(id);
        reservationService.delete(id);
        if (reservation != null) {
            reservationService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping (value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> updateReservation(@RequestBody ReservationDTO reservationDTO, @PathVariable Long id) {
        Reservation reservationForUpdate = reservationService.findOne(id);
        Reservation updatedReservation = this.convertToEntity(reservationDTO);
        reservationForUpdate.copyValues(updatedReservation);
        reservationService.update(reservationForUpdate);

        if (updatedReservation == null) {return new ResponseEntity<ReservationDTO>(HttpStatus.INTERNAL_SERVER_ERROR);}

        return new ResponseEntity<ReservationDTO>(reservationDTO, HttpStatus.OK);
    }
    @PutMapping (value = "/confirm/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> confirmReservation(@PathVariable Long id) {
        Reservation reservationForUpdate = reservationService.findOne(id);
        reservationForUpdate.setStatus(ReservationStatus.ACCEPTED);
        reservationService.update(reservationForUpdate);


        return new ResponseEntity<ReservationDTO>(convertToDto(reservationForUpdate), HttpStatus.OK);
    }
    @PutMapping (value = "/reject/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReservationDTO> rejectReservation(@PathVariable Long id) {
        Reservation reservationForUpdate = reservationService.findOne(id);
        reservationForUpdate.setStatus(ReservationStatus.REJECTED);
        reservationService.update(reservationForUpdate);


        return new ResponseEntity<ReservationDTO>(convertToDto(reservationForUpdate), HttpStatus.OK);
    }
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReservationDTO>> filterReservations(@RequestParam(required = false) String name,
                                                                             @RequestParam(required = false) Integer minimumGuests, @RequestParam(required = false) Integer maximumGuests,
                                                                             @RequestParam(required = false) Date minimumDate, @RequestParam(required = false) Date maximumDate,
                                                                             @RequestParam(required = false) Double minimumPrice, @RequestParam(required = false) Double maximumPrice) {
        Collection<Reservation> reservations = reservationService.findAll();
        Collection<ReservationDTO> reservationDTOS = reservations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<ReservationDTO>>(reservationDTOS, HttpStatus.OK);
    }

    private ReservationDTO convertToDto(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setUser(reservation.getUser().getId());
        reservationDTO.setAccommodation(reservation.getAccommodation().getId());
        reservationDTO.setNumGuests(reservation.getNumGuests());
        reservationDTO.setStartDate(reservation.getDateRange().getStartDate());
        reservationDTO.setFinishDate(reservation.getDateRange().getFinishDate());
        reservationDTO.setStatus(reservation.getStatus());
        return reservationDTO;
    }
    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Reservation reservation =  modelMapper.map(reservationDTO, Reservation.class);
        reservation.setDateRange(new DateRange(reservationDTO.getStartDate(), reservationDTO.getFinishDate()));
        reservation.setUser((Guest)userService.findOne(reservationDTO.getUser()));
        reservation.setAccommodation(accommodationService.findOne(reservationDTO.getAccommodation()));
        return reservation;
    }

}
