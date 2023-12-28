package Controllers;

import DTO.AccommodationDTO;
import DTO.FavouriteAccommodationDTO;
import Services.AccommodationService;
import Services.FavouriteAccommodationService;
import Services.IService;
import Services.UserService;
import model.Accommodation;
import model.Amenity;
import model.FavouriteAccommodation;
import model.Guest;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/api/favourites")
@ComponentScan(basePackageClasses = IService.class)
public class FavouriteAccommodationController {

    @Autowired
    private FavouriteAccommodationService favouriteAccommodationService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('GUEST')")
    @PutMapping(value = "/like-dislike/{accommodationId}/{guestId}")
    public ResponseEntity<Boolean> likeOrDislike(@PathVariable long accommodationId, @PathVariable long guestId) {
        FavouriteAccommodation favourite = this.favouriteAccommodationService.findForAccommodationAndGuest(accommodationId, guestId);
        if (favourite == null) {
            //add accommodation to favourites
            Accommodation accommodation = accommodationService.findOne(accommodationId);
            Guest guest = (Guest) userService.findOne(guestId);
            this.favouriteAccommodationService.create(new FavouriteAccommodation(0L, guest, accommodation));
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }

        //remove accommodation from favourites
        this.favouriteAccommodationService.delete(favourite.getId());
        return new ResponseEntity<Boolean>(false, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping(value = "/guest/{guestId}")
    public ResponseEntity<Collection<FavouriteAccommodation>> getFavouritesForUser(@PathVariable("guestId") Long guestId) {
        Collection<FavouriteAccommodation> favAccommodations = favouriteAccommodationService.findAllForGuest(guestId);
        //Collection<FavouriteAccommodationDTO> favAccommodationsDTOs = favAccommodations.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<FavouriteAccommodation>>(favAccommodations, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('GUEST')")
    @GetMapping(value = "/isLiked/{accommodationId}/{guestId}")
    public ResponseEntity<Boolean> isAccommodationLiked(@PathVariable long accommodationId, @PathVariable long guestId) {
        FavouriteAccommodation favourite = this.favouriteAccommodationService.findForAccommodationAndGuest(accommodationId, guestId);
        if(favourite == null) {
            return new ResponseEntity<Boolean>(false, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    private FavouriteAccommodationDTO convertToDto(FavouriteAccommodation accommodation) {
        FavouriteAccommodationDTO accommodationDTO = modelMapper.map(accommodation, FavouriteAccommodationDTO.class);

        return accommodationDTO;
    }
}
