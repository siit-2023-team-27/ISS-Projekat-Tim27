package Repositories;

import model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

public interface AccommodationRepository extends JpaRepository <Accommodation, Long> {
    Accommodation findOneById(Long id);

    Collection<Accommodation> findAllByVerified(boolean b);

    @Query("select a from Accommodation a where a.host.id = ?1")
    public Collection<Accommodation> findAllOFHost(Long hostId);
}
