package Services;

import Repositories.IRepository;
import model.Comment;
import model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ReservationService implements IService<Reservation, Long> {

    @Autowired
    private IRepository<Reservation, Long> reservationRepository;

    @Override
    public Collection<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findOne(Long id) {
        return reservationRepository.findOne(id);
    }

    @Override
    public void create(Reservation reservation) {
        reservationRepository.create(reservation);
    }

    @Override
    public void update(Reservation reservation) {
        reservationRepository.update(reservation);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.delete(id);
    }

}
