package Controllers;

import DTO.AccommodationDTO;
import DTO.ReportDTO;
import Services.IService;
import Services.ReservationService;
import Services.UserService;
import model.Accommodation;
import model.DateRange;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
@RequestMapping("/api/reports")
@ComponentScan(basePackageClasses = IService.class)
public class ReportsController {
    @Autowired
    private ReservationService reservationService;
    @GetMapping(value = "/date-range/{hostId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReportDTO>> generateReportForDateRange(@PathVariable("hostId") Long hostId,
                                                                                   @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date from,
            @RequestParam(required = true)@DateTimeFormat(pattern = "MM/dd/yyyy") Date to) {

        Collection<ReportDTO> reports = reservationService.getReportsFor(new DateRange(from, to), hostId);
        return new ResponseEntity<Collection<ReportDTO>>( reports, HttpStatus.OK);
    }
    @GetMapping(value = "/accommodation/{hostId}/{accommodationId}/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ReportDTO>> generateReportForAccommodation(@PathVariable("hostId") Long hostId,
                                                                                       @PathVariable("accommodationId") Long accommodationId,
                                                                                       @PathVariable("year") int year) {
        HashMap<Integer,ReportDTO> reports = reservationService.getReportsFor(year, accommodationId, hostId);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        for (Integer key: reports.keySet()){
            ReportDTO r = reports.get(key);
            r.setMonth(key);
            reportDTOS.add(r);
        }
        return new ResponseEntity<Collection<ReportDTO>>(reportDTOS,HttpStatus.OK);
    }
}
