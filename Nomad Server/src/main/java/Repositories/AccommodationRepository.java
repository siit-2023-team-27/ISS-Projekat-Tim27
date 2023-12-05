package Repositories;

import model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AccommodationRepository extends JpaRepository <Accommodation, Long> {
    Accommodation findOneById(Long id);

}
