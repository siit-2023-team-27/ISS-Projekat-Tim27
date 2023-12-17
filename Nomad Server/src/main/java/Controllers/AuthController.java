package Controllers;

import DTO.LoginDTO;
import DTO.UserDTO;
import DTO.UserRegistrationDTO;
import DTO.UserTokenState;
import Services.ConfirmationTokenService;
import Services.MailService;
import Services.UserService;
import exceptions.ResourceConflictException;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import model.enums.UserType;
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
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import util.Helper;
import util.TokenUtils;

import java.io.IOException;

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
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    MailService mailService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody LoginDTO loginDto) {
        // false credentials exception
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()));

        // add to security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create token
        AppUser user = (AppUser) userService.loadUserByUsername(loginDto.getUsername());

        String jwt = tokenUtils.generateToken(user.getId(), user.getUsername(), user.getAuthorities());
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<AppUser> addUser(@RequestBody UserRegistrationDTO userDTO) throws IOException {
        if(!Helper.isEmailPatternValid(userDTO.getUsername()) || !Helper.isPasswordValid(userDTO.getPassword())
                || !userDTO.getPassword().equals(userDTO.getPasswordConfirmation())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean existUser = this.userService.isRegistrated(userDTO.getUsername());

        if (existUser) {
            throw new ResourceConflictException(null,"Username already exists");
        }
        AppUser user = null;
        if(userDTO.getRoles().get(0)== UserType.GUEST){
            user = convertToGuest(userDTO);
        }else if(userDTO.getRoles().get(0)== UserType.HOST){
            user = convertToHost(userDTO);
        }
        this.userService.create(user);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.create(confirmationToken);

        mailService.sendVerificationLink(confirmationToken, user.getUsername());

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @GetMapping(value="/confirm-account")
    public RedirectView confirmUserAccount( @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

        if(token != null && !token.isTokenExpired())
        {
            AppUser user = (AppUser)userService.loadUserByUsername(token.getUserEntity().getUsername());
            user.setVerified(true);
            userService.save(user);
            //return new ResponseEntity<AppUser>(user, HttpStatus.OK);
            return new RedirectView("http://localhost:4200/login");
        }
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new RedirectView("http://localhost:4200/login");
    }
    private UserRegistrationDTO convertToDto(AppUser user) {
        UserRegistrationDTO userDTO = modelMapper.map(user, UserRegistrationDTO.class);

        return userDTO;
    }
    private Host convertToHost(UserRegistrationDTO userDTO) {
        return modelMapper.map(userDTO, Host.class);
    }
    private Guest convertToGuest(UserRegistrationDTO userDTO) {
        return modelMapper.map(userDTO, Guest.class);
    }
}