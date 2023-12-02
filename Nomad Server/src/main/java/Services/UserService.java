package Services;

import Repositories.UserRepository;
import model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;


import java.util.Collection;

@Service
@ComponentScan("Repositories")
@EnableJpaRepositories("Repositories")
public class UserService implements IService<AppUser, Long> {

    @Autowired
    private UserRepository userRepository;

//    public User getExisting(LoginDTO loginDto){
//        return userRepository.getExisting(loginDto);
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
