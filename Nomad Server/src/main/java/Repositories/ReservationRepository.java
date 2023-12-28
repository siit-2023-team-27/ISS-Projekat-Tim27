package Repositories;

import model.DateRange;
import model.Reservation;
import model.ReservationDate;
import model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findOneById(Long id);


    Collection<Reservation> findAllByAccommodation_Host_id(long userId);


    Collection<Reservation> findAllByGuest_id(long userId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.status = :status " +
            "AND :currentDate BETWEEN r.dateRange.startDate AND r.dateRange.finishDate " +
            "AND r.guest.id = :guestId")
    Collection<Reservation> findAllActiveByGuestId(
            @Param("status") ReservationStatus status,
            @Param("currentDate") Date currentDate,
            @Param("guestId") Long guestId
    );

    Collection<Reservation> findAllByAccommodation_id(long accommodationId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.status = :status " +
            "AND :currentDate <= r.dateRange.startDate " +
            "AND r.guest.id = :hostId")
    Collection<Reservation> findAllFutureByHost(
            @Param("status") ReservationStatus status,
            @Param("currentDate") Date currentDate,
            @Param("hostId") Long hostId
    );

}
