package Controllers;

import DTO.AccommodationDTO;
import Services.IService;
import model.Accommodation;
import model.UserReport;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/user_reports")
@ComponentScan(basePackageClasses = IService.class)
public class UserReportController {

    @Autowired
    private IService<UserReport, Long> userReportService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserReport>> getUserReports() {
        Collection<UserReport> userReports = userReportService.findAll();
        return new ResponseEntity<Collection<UserReport>>(userReports, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReport> getUserReport(@PathVariable("id") Long id) {
        UserReport userReport = userReportService.findOne(id);

        if (userReport == null) {
            return new ResponseEntity<UserReport>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReport> createUserReport(@RequestBody UserReport userReport) throws Exception {
        userReportService.create(userReport);
        return new ResponseEntity<UserReport>(userReport, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserReport> deleteUserReport(@PathVariable("id") Long id) {
        userReportService.delete(id);
        return new ResponseEntity<UserReport>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReport> updateUserReport(@RequestBody UserReport userReport, @PathVariable Long id)
            throws Exception {
        UserReport userReportForUpdate = userReportService.findOne(id);;
        userReportForUpdate.copyValues(userReport);

        userReportService.update(userReportForUpdate);

        if (userReportForUpdate == null) {
            return new ResponseEntity<UserReport>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserReport>(userReport, HttpStatus.OK);
    }

}
