package Services;

import Repositories.*;
import model.Accommodation;
import model.Amenity;
import model.ReservationDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ComponentScan("Repositories")
@EnableJpaRepositories("Repositories")
public class AccommodationService implements IService<Accommodation, Long> {

    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private ReservationDateRepository reservationDateRepository;

    @Override
    public Collection<Accommodation> findAll() {
        return accommodationRepository.findAll();
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

    public boolean isAvailable(long accommodationId, Date date){
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if (reservationDate == null){
            return true;
        }else{
            return reservationDate.getReservation() == null;
        }
    }
//    public List<LocalDate> getTakenDates(long accommodationId, Date start, Date end){
//        LocalDate startLocal = LocalDate.from(start.toInstant());
//        LocalDate endLocal = LocalDate.from(end.toInstant());
//        return startLocal.datesUntil(endLocal).filter(date -> {
//            return this.isAvailable(accommodationId, Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        }).collect(Collectors.toList());
//    }
    public List<Date> getTakenDates(long accommodationId){
        List<ReservationDate> reservationDates = reservationDateRepository.findAllByAccommodation_id(accommodationId);
        return reservationDates.stream().filter(reservationDate -> {
            return reservationDate.getReservation()!=null;
        }).map(ReservationDate::getDate).collect(Collectors.toList());
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
    public Double getPrice(long accommodationId, Date date) {
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if (reservationDate == null){
            return 100D;
        }else{
            return reservationDate.getPrice();
        }
    }
}
