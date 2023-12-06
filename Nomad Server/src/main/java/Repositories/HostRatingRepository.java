package Repositories;

import model.HostRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRatingRepository extends JpaRepository<HostRating, Long> {
    HostRating findOneById(Long id);
}
