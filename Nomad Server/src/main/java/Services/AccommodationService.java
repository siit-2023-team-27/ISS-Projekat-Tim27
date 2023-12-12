package Services;

import Repositories.*;
import model.*;
import model.enums.AccommodationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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

    public Collection<Accommodation> getSearchedAndFiltered(String city, DateRange dateRange, int peopleNum, Double minimumPrice,
                                                     Double maximumPrice, List<Amenity> amenity, AccommodationType type) {
        Collection<Accommodation> filtered = new ArrayList<>();
        for (Accommodation a: this.getFilteredAccommodations(peopleNum, city, type, amenity)) {
            System.out.println("NASAAO APPPP");
            if(this.isAvailable(a, dateRange, minimumPrice, maximumPrice)){
                filtered.add(a);
            }
        }
        return filtered;
    }

    public boolean isAvailable(long accommodationId, Date date){
        ReservationDate reservationDate = reservationDateRepository.findByAccommodation_IdAndDate(accommodationId, date);
        if (reservationDate == null){
            return true;
        }else{
            return reservationDate.getReservation() == null;
        }
    }
    public List<Accommodation> getFilteredAccommodations(int peopleNum, String city, AccommodationType accommodationType, List<Amenity> amenities){

        return accommodationRepository.findAllBy(peopleNum, city, accommodationType, amenities);
        //, city, accommodationType, amenities
    }

    public boolean isAvailable(long accommodationId, Date date, Double priceMin, Double priceMax){
        ReservationDate reservationDate = reservationDateRepository.findBy(accommodationId, date, priceMin, priceMax);
        System.out.println("NAASAO RES DATE"+reservationDate);
        //dodati upit za min, max da ima
        if (reservationDate == null){
            //ako nema u resDate proveri da li cena odgovara za defaultnu cenu
            //znaci da nije nasao ni rezervaciju za taj datum
            if(priceMin != null && priceMax != null){
                Accommodation accommodation = accommodationRepository.findOneById(accommodationId);
                if(accommodation.getDefaultPrice()<=priceMax && accommodation.getDefaultPrice()>=priceMin){
                    return true;
                }else{
                    return false;
                }
            } return true;
        }
            return reservationDate.getReservation() == null;
    }

    public boolean isAvailable(Accommodation accommodation, DateRange dateRange, Double priceMin, Double priceMax){
        //proverava u ReservationDateRepo
        //ukoliko ima postavljena dodatna cena za neki od datuma iz DateRange
        Calendar c = Calendar.getInstance();
        c.setTime(dateRange.getStartDate());
        for(; c.getTime().before(dateRange.getFinishDate()); c.add(Calendar.DATE, 1)){
            if (!this.isAvailable(accommodation.getId(), c.getTime(), priceMin, priceMax) ){
                return false;
            }
        }
        return true;
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
}
