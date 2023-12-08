package Services;

import Repositories.IRepository;
import Repositories.ReservationRepository;
import model.Accommodation;
import model.Comment;
import model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class ReservationService implements IService<Reservation, Long> {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> findAllForAccommodation(Accommodation accommodation){
        return reservationRepository.findAllForAccommodation(accommodation);
    }
    @Override
    public Collection<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findOne(Long id) {
        return reservationRepository.findOneById(id);
    }

    @Override
    public void create(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void update(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

}
