package Repositories;

import model.AccommodationRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRatingRepository extends JpaRepository<AccommodationRating, Long> {
    AccommodationRating findOneById(Long id);
}
