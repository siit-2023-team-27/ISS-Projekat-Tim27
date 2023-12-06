package Repositories;

import model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

public interface AccommodationRepository extends JpaRepository <Accommodation, Long> {
    Accommodation findOneById(Long id);

    Collection<Accommodation> findAllByVerified(boolean b);
}
