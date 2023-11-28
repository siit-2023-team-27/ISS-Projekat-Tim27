package Repositories;

import DTO.LoginDTO;
import model.Accommodation;
import model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Repository
public class InMemoryUserRepository implements IUserRepository {
    private ConcurrentMap<Long, User> users = new ConcurrentHashMap<Long, User>();
    private static Long id = 0l;
    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    public User getExisting(LoginDTO loginDTO) {
        for(User u: users.values()){
            if(u.getUsername().equals(loginDTO.getUsername()) && u.getPassword().equals(loginDTO.getPassword())){
                return u;
            }
        }
        return null;
    }
    @Override
    public void create(User object) {
        this.users.put(id, object);
        object.setId(id++);
    }

    @Override
    public User findOne(Long id) {
        return this.users.get(id);
    }

    @Override
    public void update(User object) {
        this.users.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.users.remove(id);
    }
}
