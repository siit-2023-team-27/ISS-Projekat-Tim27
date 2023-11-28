package Services;

import DTO.LoginDTO;
import Repositories.IRepository;
import Repositories.IUserRepository;
import Repositories.InMemoryUserRepository;
import model.Reservation;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService implements IService<User, Long> {

    @Autowired
    private IUserRepository userRepository;

    public User getExisting(LoginDTO loginDto){
        return userRepository.getExisting(loginDto);
    }
    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public void create(User user) {
        userRepository.create(user);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

}
