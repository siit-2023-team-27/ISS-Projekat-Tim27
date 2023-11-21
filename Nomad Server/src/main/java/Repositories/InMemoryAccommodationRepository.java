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
    }

    @Override
    public Accommodation findOne(Long id) {
        return null;
    }

    @Override
    public Accommodation update(Long object) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
