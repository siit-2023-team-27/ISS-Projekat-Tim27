package Controllers;

import Services.IService;
import model.AccommodationRating;
import model.HostRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class AccommodationRatingController {

    @Autowired
    private IService<AccommodationRating, Long> accommodationRatingService;
}
