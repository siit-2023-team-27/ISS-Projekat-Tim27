package Services;

import DTO.LoginDTO;
import Repositories.IRepository;
import Repositories.UserRepository;
import model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class UserService implements IService<AppUser, Long>, UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    // Funkcija koja na osnovu username-a iz baze vraca objekat User-a
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }
//    public AppUser findByUsername(String username){
//        return userRepository.findOneByUsername(username);
//    }

    @Override
    public Collection<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public AppUser findOne(Long id) {
        return userRepository.findOneById(id);
    }

    @Override
    public void create(AppUser appUser) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
    }

    @Override
    public void update(AppUser appUser) {
        userRepository.save(appUser);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
