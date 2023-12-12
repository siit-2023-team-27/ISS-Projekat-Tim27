package Repositories;

import model.Accommodation;
import model.Amenity;
import model.enums.AccommodationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

public interface AccommodationRepository extends JpaRepository <Accommodation, Long> {
    Accommodation findOneById(Long id);

    Collection<Accommodation> findAllByVerified(boolean b);

    @Query("select a from Accommodation a " +
            "where a.maxGuests >=:peopleNum and a.minGuests<=:peopleNum " +
            "and a.address like CONCAT('%', :city, '%') " +
            "and (:type IS NULL OR a.accommodationType = :type ) " +
            "and (:amenities IS NULL OR :amenities MEMBER OF a.amenities)")
    List<Accommodation> findAllBy(@Param("peopleNum")int peopleNum, @Param("city")String city,
                                  @Param("type")AccommodationType accommodationType, @Param("amenities") List<Amenity> amenities);
}
//" +
//        "and a.address like :city and (:type IS NULL OR a.accommodationType = :type )" +
//        "and (:amenities IS NULL OR :amenities MEMBER OF a.amenities)

//, @Param("city")String city,
//@Param("type")AccommodationType accommodationType, @Param("amenities") List<String> amenities