package Repositories;

import DTO.ReservationDTO;
import model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findOneById(Long id);


    Collection<Reservation> findAllByAccommodation_Host_id(long userId);


    Collection<Reservation> findAllByGuest_id(long userId);
}
