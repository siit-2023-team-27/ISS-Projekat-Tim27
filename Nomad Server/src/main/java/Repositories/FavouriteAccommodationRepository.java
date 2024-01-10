package Repositories;

import model.Amenity;
import model.FavouriteAccommodation;
import model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FavouriteAccommodationRepository extends JpaRepository<FavouriteAccommodation, Long> {

    FavouriteAccommodation findOneById(Long id);
    Collection<FavouriteAccommodation> findAllByGuest_id(Long guestId);
}
