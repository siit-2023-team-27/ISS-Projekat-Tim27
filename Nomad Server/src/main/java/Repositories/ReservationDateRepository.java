package Repositories;

import model.Accommodation;
import model.Amenity;
import model.Reservation;
import model.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Long> {

    ReservationDate findByAccommodation_IdAndDate(long accommodationId, Date date);
}
