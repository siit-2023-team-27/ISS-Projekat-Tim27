package Repositories;

import model.Accommodation;
import model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Repository
public class InMemoryReservationRepository implements IRepository<Reservation, Long> {
    private ConcurrentMap<Long, Reservation> reservations = new ConcurrentHashMap<Long, Reservation>();
    private static Long id = 0l;
    @Override
    public Collection<Reservation> findAll() {
        return reservations.values();
    }

    @Override
    public void create(Reservation object) {
        this.reservations.put(id, object);
        object.setId(id++);
    }

    @Override
    public Reservation findOne(Long id) {
        return this.reservations.get(id);
    }

    @Override
    public void update(Reservation object) {
        this.reservations.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.reservations.remove(id);
    }
}
