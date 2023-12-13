package Repositories;

import model.Accommodation;
import model.Amenity;
import model.Reservation;
import model.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationDateRepository extends JpaRepository<ReservationDate, Long> {

    ReservationDate findByAccommodation_IdAndDate(long accommodationId, Date date);
    @Query("select r from ReservationDate r " +
            "where r.accommodation.id=:id and r.date = :date " +
            "and (r.accommodation.priceType = 1 OR (r.price IS NULL OR (:minPrice IS NULL OR r.price >=:minPrice and r.price<=:maxPrice)))" +
            "and (r.accommodation.priceType = 0 OR (r.price IS NULL OR (:minPrice IS NULL OR r.price/:peopleNum >=:minPrice and r.price/:peopleNum<=:maxPrice)))")
    ReservationDate findBy(@Param("id")long accommodationId,@Param("date") Date date,
                           @Param("minPrice")Double minPrice, @Param("maxPrice")Double maxPrice, @Param("peopleNum")int peopleNum);

    List<ReservationDate> findAllByAccommodation_id(long accommodationId);

    boolean existsByAccommodation_IdAndDate(long id, Date date);

    ReservationDate findOneByAccommodation_IdAndDate(long id, Date date);

}
