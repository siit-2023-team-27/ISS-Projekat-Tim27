package Controllers;

import DTO.AccommodationDTO;
import DTO.LoginDTO;
import DTO.LoginResponseDTO;
import DTO.UserDTO;
import Services.AccommodationService;
import Services.IService;
import Services.ReservationService;
import Services.UserService;
import exceptions.ResourceConflictException;
import model.Admin;
import model.AppUser;
import model.Guest;

import model.Host;

import model.enums.UserType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

import static model.enums.UserType.ADMIN;

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
@RequestMapping("/api/users")
@ComponentScan(basePackageClasses = IService.class)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    AccommodationService accommodationService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserDTO>> getUsers() {
        Collection<AppUser> appUsers = userService.findAll();
        Collection<UserDTO> userDTOS = appUsers.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<UserDTO>>(userDTOS, HttpStatus.OK);
    }

    //config
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        AppUser appUser = userService.findOne(id);

        if (appUser == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserDTO>(this.convertToDto(appUser), HttpStatus.OK);
    }

    //config
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) throws Exception {
        boolean existUser = this.userService.isRegistrated(userDTO.getUsername());
        if (existUser) {
            throw new ResourceConflictException(null,"Username already exists");
        }

        AppUser user = null;
        if(userDTO.getRoles().get(0)== UserType.GUEST){
            user = convertToEntityGuest(userDTO);
        } else if(userDTO.getRoles().get(0)== UserType.HOST){
            user = convertToEntityHost(userDTO);
        }else{
            user = convertToEntityAdmin(userDTO);
        }
        userService.create(user);
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
    }
    //config
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteAccommodation(@PathVariable("id") Long id) {
        AppUser user = userService.findOne(id);
        UserDTO userDto = convertToDto(user);
        switch (userDto.getRoles().get(0)) {
            case HOST:
                if (reservationService.findActiveReservationsForHost(user.getId()).size() > 0) {
                    return new ResponseEntity<String>("This account cannot be deleted, because host has active reservations.", HttpStatus.OK);
                } else {
                    accommodationService.deleteAllForHost(id);
                }
            case GUEST:
                if (reservationService.findActiveReservationsForGuest(user.getId()).size() > 0) {
                    return new ResponseEntity<String>("This account cannot be deleted, because guest has active reservations.", HttpStatus.OK);
                }
        }
        return new ResponseEntity<String>("This account cannot be deleted, because guest has active reservations.", HttpStatus.OK);
    }
    //config
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id)
            throws Exception {
        AppUser appUserForUpdate = userService.findOne(id);
        AppUser updatedAppUser;
        switch (userDTO.getRoles().get(0)){
            case ADMIN:
                updatedAppUser = this.convertToEntityAdmin(userDTO);
                break;
            case HOST:
                updatedAppUser = this.convertToEntityHost(userDTO);
                break;
            case GUEST:
                updatedAppUser = this.convertToEntityGuest(userDTO);
                break;
            default:
                updatedAppUser = this.convertToEntity(userDTO);
        }

        appUserForUpdate.copyValues(updatedAppUser);

        userService.update(appUserForUpdate);

        if (updatedAppUser == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/suspend/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> suspendUser(@PathVariable Long id) {
        AppUser user = userService.findOne(id);
        user.setSuspended(true);
        userService.update(user);
        return new ResponseEntity<UserDTO>(HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/un-suspend/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> unsuspentUser(@PathVariable Long id) {
        AppUser user = userService.findOne(id);
        user.setSuspended(false);
        userService.update(user);
        return new ResponseEntity<UserDTO>(HttpStatus.OK);
    }
    private UserDTO convertToDto(AppUser appUser) {
        ArrayList<UserType> roles = new ArrayList<UserType>();
        UserDTO userDTO = modelMapper.map(appUser, UserDTO.class);
        if(appUser.getClass() == Admin.class){
            roles.add(UserType.ADMIN);
        }else if(appUser.getClass() == Guest.class){
            roles.add(UserType.GUEST);
        }else{
            roles.add(UserType.HOST);
        }
        userDTO.setRoles(roles);

        return userDTO;
    }
    private AppUser convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, AppUser.class);
    }
    private Admin convertToEntityAdmin(UserDTO userDTO) {
        return modelMapper.map(userDTO, Admin.class);
    }
    private Host convertToEntityHost(UserDTO userDTO) {
        return modelMapper.map(userDTO, Host.class);
    }

    private Guest convertToEntityGuest(UserDTO userDTO) {
        return modelMapper.map(userDTO, Guest.class);
    }
}
