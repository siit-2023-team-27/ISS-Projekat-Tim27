package interceptor;

import Controllers.AccommodationController;
import DTO.UserRegistrationDTO;
import Repositories.UserRepository;
import Services.UserService;
import config.WebSecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.AppUser;
import model.Guest;
import model.Host;
import model.enums.UserType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import security.auth.RestAuthEntryPoint;
import util.TokenUtils;

import java.util.*;

@Component
@ComponentScan(basePackageClasses = {UserService.class, ModelMapper.class, UserRepository.class})
public class KeycloakInterceptor implements HandlerInterceptor {

    UserService userService;
    @Autowired
    ModelMapper modelMapper;
    private final Object lock = new Object();


    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
    private Host convertToHost(UserRegistrationDTO userDTO) {
        Host host = modelMapper.map(userDTO, Host.class);
        host.setNotificationPreferences(new HashMap<>());
        return host;
    }
    private Guest convertToGuest(UserRegistrationDTO userDTO) {
        Guest guest = modelMapper.map(userDTO, Guest.class);
        guest.setCancellationNumber(0);
        guest.setNotificationPreferences(new HashMap<>());

        guest.setNotificationPreferences(new HashMap<>());

        return guest;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("Inside of PreHandle method, running before passing request controller");
        String token = getToken(request);
        System.out.println(token);
        if(token == null){
            return false;
        }
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        UserRegistrationDTO user = new UserRegistrationDTO();

        String[] fields = payload.split(",");
        for (String field: fields) {
            String[] ee = field.split(":");
            if(ee.length > 1){
                ee[0] = ee[0].replace("\"", "");
                ee[1] = ee[1].replace("\"", "");
                if(ee[0].equals("adress")){
                    user.setAddress(ee[1]);
                }else if(ee[0].equals("phone")){
                    user.setPhoneNumber(ee[1]);
                }else if(ee[0].equals("email")){
                    ee[1] = ee[1].replace("}", "");
                    user.setUsername(ee[1]);
                }else if(ee[0].equals("name")){
                    String[] ee2 = field.split(" ");
                    user.setLastName(ee2[1]);
                    user.setFirstName(ee2[0]);
                }else if(ee[0].equals("role")){
                    List<UserType> roles = new ArrayList<>();
                    roles.add(UserType.valueOf(ee[1]));
                    user.setRoles(roles);
                }
            }

        }
        synchronized (lock) {
            Long id = this.userService.getIdByUsername(user.getUsername());
            if (id != null) {
                return false;
            }
            user.setPassword("");
            user.setPasswordConfirmation("");
            AppUser user2 = null;
            //modelMapper = new ModelMapper();
            if (user.getRoles().get(0) == UserType.GUEST) {
                user2 = convertToGuest(user);
            } else if (user.getRoles().get(0) == UserType.HOST) {
                user2 = convertToHost(user);
            }
            user2.setVerified(true);
            //this.userService = new UserService(null);
            this.userService.save(user2);
            System.out.println(user);
        }
        return true;
    }

}
