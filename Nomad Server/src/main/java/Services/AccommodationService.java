package Services;

import DTO.AccommodationDTO;
import Repositories.*;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@ComponentScan("Repositories")
@EnableJpaRepositories("Repositories")
public class AccommodationService implements IService<Accommodation, Long> {

    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Collection<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    public Collection<Accommodation> search(String city, DateRange dateRange, int peopleNum) {
        Collection<Accommodation> accommodations = accommodationRepository.findAll();
        Collection<Accommodation> filtered = new ArrayList<>();
        for (Accommodation a: accommodations) {
            if(city.equals(a.getAddress()) && isAvailableFor(dateRange, a) && peopleNum <= a.getMaxGuests() && peopleNum >= a.getMinGuests()){
                filtered.add(a);
            }
        }
        return filtered;
    }
    public boolean isAvailableFor(DateRange dateRange, Accommodation accommodation){
        Collection<Reservation> reservations = reservationRepository.findAllForAccommodation(accommodation);
        for (Reservation r: reservations) {
            if(r.getDateRange().overlaps(dateRange)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Accommodation findOne(Long id) {
        return accommodationRepository.findOneById(id);
    }

    @Override
    public void create(Accommodation accommodation) {
        accommodationRepository.save(accommodation);
    }

    @Override
    public void update(Accommodation accommodation) {
        accommodationRepository.save(accommodation);
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.deleteById(id);
    }

    public List<Amenity> getAllAmenitiesForAccommodation(long accommodationId) {
        Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
        return accommodation.getAmenities();
    }
    public Collection<Accommodation> getUnverifiedAccommodations() {return accommodationRepository.findAllByVerified(false);}

    public void addAmenityToAccommodation (long accommodationId, Amenity newAmenity) {
        Amenity savedAmenity = amenityRepository.save(newAmenity);
        Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
        List<Amenity> amenities = accommodation.getAmenities();
        amenities.add(savedAmenity);
        accommodation.setAmenities(amenities);
        accommodationRepository.save(accommodation);
    }
}
