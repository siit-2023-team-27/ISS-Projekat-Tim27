package Repositories;

import model.AccommodationRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface AccommodationRatingRepository extends JpaRepository<AccommodationRating, Long> {
    AccommodationRating findOneById(Long id);



    Collection<AccommodationRating> findAllByAccommodation_Id(Long id);
}
