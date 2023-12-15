package Controllers;

import DTO.AccommodationDTO;
import DTO.LoginDTO;
import DTO.LoginResponseDTO;
import DTO.UserDTO;
import Services.IService;
import Services.UserService;
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
    private ModelMapper modelMapper;
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserDTO>> getUsers() {
        Collection<AppUser> appUsers = userService.findAll();
        Collection<UserDTO> userDTOS = appUsers.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<UserDTO>>(userDTOS, HttpStatus.OK);
    }
   // @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        AppUser appUser = userService.findOne(id);

        if (appUser == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserDTO>(this.convertToDto(appUser), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createAccommodation(@RequestBody UserDTO userDTO) throws Exception {
        Guest appUser = this.convertToEntityGuest(userDTO);
        userService.create(appUser);
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
    }


    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) throws Exception {
        AppUser user = (AppUser) userService.loadUserByUsername(loginDTO.getUsername());
        if(user != null){
            LoginResponseDTO response = user.toLoginResponse();
            return new ResponseEntity<LoginResponseDTO>(response, HttpStatus.OK);
        }
        return new ResponseEntity<LoginResponseDTO>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UserDTO> deleteAccommodation(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<UserDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateAccommodation(@RequestBody UserDTO userDTO, @PathVariable Long id)
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
    @PutMapping(value = "/suspend/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> suspendUser(@PathVariable Long id) {

        return new ResponseEntity<UserDTO>(HttpStatus.OK);
    }
    @PutMapping(value = "/un-suspend/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> unsuspentUser(@PathVariable Long id) {

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
