package Repositories;

import model.Accommodation;
import model.Amenity;
import model.Reservation;
import model.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Long> {

    ReservationDate findByAccommodation_IdAndDate(long accommodationId, Date date);

    List<ReservationDate> findAllByAccommodation_id(long accommodationId);

    boolean existsByAccommodation_IdAndDate(long id, Date date);

    ReservationDate findOneByAccommodation_IdAndDate(long id, Date date);

    void deleteByReservation_id(Long id);
}
