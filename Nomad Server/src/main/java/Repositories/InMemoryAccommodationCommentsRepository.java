package Repositories;

import model.Accommodation;
import model.AccommodationComment;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAccommodationCommentsRepository implements IRepository<AccommodationComment, Long> {
    private ConcurrentMap<Long, AccommodationComment> accommodationComments = new ConcurrentHashMap<Long, AccommodationComment>();
    private static Long id = 0l;

    @Override
    public Collection<AccommodationComment> findAll() {
        return this.accommodationComments.values();
    }

    @Override
    public void create(AccommodationComment object) {
        this.accommodationComments.put(id, object);
        object.setId(id++);
    }

    @Override
    public AccommodationComment findOne(Long id) {
        return this.accommodationComments.get(id);
    }

    @Override
    public void update(AccommodationComment object) {
        this.accommodationComments.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.accommodationComments.remove(id);
    }
}
