package Repositories;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import model.Accommodation;

public class InMemoryAccommodationRepository implements IRepository<Accommodation, Long> {
    private ConcurrentMap<Long, Accommodation> accommodation;
    private static Long id = 0l;
    @Override
    public Collection<Accommodation> findAll() {
        return accommodation.values();
    }

    @Override
    public void create(Accommodation object) {
        this.accommodation.put(id, object);
        object.setId(id++);
    }

    @Override
    public Accommodation findOne(Long id) {
        return this.accommodation.get(id);
    }

    @Override
    public void update(Accommodation object) {
        this.accommodation.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.accommodation.remove(id);
    }
}
