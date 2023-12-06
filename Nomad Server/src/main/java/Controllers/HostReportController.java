package Controllers;

import Services.AccommodationService;
import Services.IService;
import model.Accommodation;
import model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("/api/host-report")
@ComponentScan(basePackageClasses = IService.class)
public class HostReportController {
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private IService<Reservation, Long> reservationService;

    //zadatog perioda dobije izveštaje o broju rezervacija i
    //ukupan profit koji je svaki od njegovih smeštaja
    @GetMapping(value = "/{idHost}/allAccommodations/{dateFrom}/{dateTo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getReportForAllAccommodations(@PathVariable("idHost") Long idHost, @PathVariable("dateFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFrom,
                                              @PathVariable("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTo) {

        Collection<Accommodation> accommodations = accommodationService.findAllOFHost(idHost);


        return 0;
    }


    //izabran smeštaj dobije izveštaj o broju
    //rezervacija i ukupan profit koji je ostvario u svakom mesecu u jednoj godin

    @GetMapping(value = "/accommodation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public long getReportForAccomodation(@PathVariable("id") Long id) {

        return 0;
    }

}