package Repositories;

import model.Accommodation;
import model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findOneById(Long id);
    @Query("select r from Reservation r where r.accommodation = ?1")
    List<Reservation> findAllForAccommodation(Accommodation accommodation);
}
