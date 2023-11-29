package Repositories;

import model.Accommodation;
import model.AccommodationRating;
import model.HostRating;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHostRatingRepository implements IRepository<HostRating, Long>{
    private ConcurrentMap<Long, HostRating> hostRatings = new ConcurrentHashMap<Long, HostRating>();
    private static Long id = 0l;

    @Override
    public Collection<HostRating> findAll() {
        return this.hostRatings.values();
    }

    @Override
    public void create(HostRating object) {
        this.hostRatings.put(id, object);
        object.setId(id++);
    }

    @Override
    public HostRating findOne(Long id) {
        return this.hostRatings.get(id);
    }

    @Override
    public void update(HostRating object) {
        this.hostRatings.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.hostRatings.remove(id);
    }
}
