package Repositories;

import DTO.LoginDTO;
import model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Repository
public class InMemoryUserRepository implements IUserRepository {
    private ConcurrentMap<Long, AppUser> users = new ConcurrentHashMap<Long, AppUser>();
    private static Long id = 0l;
    @Override
    public Collection<AppUser> findAll() {
        return users.values();
    }

    public AppUser getExisting(LoginDTO loginDTO) {
        for(AppUser u: users.values()){
            if(u.getUsername().equals(loginDTO.getUsername()) && u.getPassword().equals(loginDTO.getPassword())){
                return u;
            }
        }
        return null;
    }
    @Override
    public void create(AppUser object) {
        this.users.put(id, object);
        object.setId(id++);
    }

    @Override
    public AppUser findOne(Long id) {
        return this.users.get(id);
    }

    @Override
    public void update(AppUser object) {
        this.users.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.users.remove(id);
    }
}
