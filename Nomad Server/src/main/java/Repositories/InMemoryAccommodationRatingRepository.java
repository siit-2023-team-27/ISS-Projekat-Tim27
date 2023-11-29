package Repositories;

import model.AccommodationRating;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAccommodationRatingRepository implements IRepository<AccommodationRating, Long>{

    private ConcurrentMap<Long, AccommodationRating> accommodationRatings = new ConcurrentHashMap<Long, AccommodationRating>();
    private static Long id = 0l;

    @Override
    public Collection<AccommodationRating> findAll() {
        return this.accommodationRatings.values();
    }

    @Override
    public void create(AccommodationRating object) {
        this.accommodationRatings.put(id, object);
        object.setId(id++);
    }

    @Override
    public AccommodationRating findOne(Long id) {
        return this.accommodationRatings.get(id);
    }

    @Override
    public void update(AccommodationRating object) {
        this.accommodationRatings.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.accommodationRatings.remove(id);
    }
}
