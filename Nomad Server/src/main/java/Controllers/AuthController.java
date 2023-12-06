package Controllers;

import DTO.LoginDTO;
import DTO.UserDTO;
import DTO.UserRegistrationDTO;
import DTO.UserTokenState;
import Services.UserService;
import exceptions.ResourceConflictException;
import jakarta.servlet.http.HttpServletResponse;
import model.AppUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import util.TokenUtils;
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
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody LoginDTO loginDto) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        AppUser user = (AppUser) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthorities());
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<AppUser> addUser(@RequestBody UserRegistrationDTO userDTO) {
        UserDetails existUser = this.userService.loadUserByUsername(userDTO.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(existUser.getUsername(), "Username already exists");
        }
        AppUser user = convertToEntity(userDTO);
        this.userService.create(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    private UserRegistrationDTO convertToDto(AppUser user) {
        UserRegistrationDTO userDTO = modelMapper.map(user, UserRegistrationDTO.class);

        return userDTO;
    }
    private AppUser convertToEntity(UserRegistrationDTO userDTO) {
        return modelMapper.map(userDTO, AppUser.class);
    }
}