package Services;

import DTO.AccommodationDTO;
import DTO.SearchAccommodationDTO;
import Repositories.*;
import model.*;
import model.enums.AccommodationType;
import model.enums.PriceType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Collection<Accommodation> findAll() {
        return  accommodationRepository.findAll();
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

    public Collection<Accommodation> findByHost(Long host) { return this.accommodationRepository.findAllByHost_id(host); }

    public Collection<SearchAccommodationDTO> getSearchedAndFiltered(String city, DateRange dateRange, int peopleNum, Double minimumPrice,
                                                     Double maximumPrice, List<Long> amenity, AccommodationType type) {
        Collection<SearchAccommodationDTO> filtered = new ArrayList<>();
        for (Accommodation a: this.getFilteredAccommodations(peopleNum, city, type, amenity)) {
            SearchAccommodationDTO accommodationDTO = this.checkAvailability(a, dateRange, minimumPrice, maximumPrice, peopleNum);
            if(accommodationDTO != null){
                filtered.add(accommodationDTO);
            }
        }
        return filtered;
    }
    public Collection<Accommodation> getFiltered(Double minimumPrice, Double maximumPrice, List<Long> amenity, AccommodationType type) {
        Collection<Accommodation> filtered = new ArrayList<>();

        for (Accommodation a: this.getFilteredAccommodations(type, amenity)) {
            if(minimumPrice != null && maximumPrice != null){
                if(isPriceInRange(a,minimumPrice, maximumPrice, 1)){
                    filtered.add(a);
                }
            }else{
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
    public List<Accommodation> getFilteredAccommodations(int peopleNum, String city, AccommodationType accommodationType, List<Long> amenities){
        List<Accommodation> accommodations =  accommodationRepository.findAllBy(peopleNum, city, accommodationType);
        List<Accommodation> filtered = new ArrayList<>();
        for (Accommodation a: accommodations) {
            if(hasAllAmenities(a, amenities)){
                filtered.add(a);
            }
        }
        return filtered;
    }
    public List<Accommodation> getFilteredAccommodations(AccommodationType accommodationType, List<Long> amenities){
        List<Accommodation> accommodations =  accommodationRepository.findAllBy(accommodationType);
        List<Accommodation> filtered = new ArrayList<>();
        for (Accommodation a: accommodations) {
            if(hasAllAmenities(a, amenities)){
                filtered.add(a);
            }
        }
        return filtered;
    }

    public boolean hasAllAmenities(Accommodation accommodation, List<Long> amenities){
        int found = 0;
        if(amenities == null){
            return true;
        }
        for (Long id: amenities) {
            for (Amenity amenity: accommodation.getAmenities()) {
                if (id == amenity.getId()){
                    found++;
                    break;
                }
            }
        }
        if(found == amenities.size()){
            return true;
        }
        return false;
    }

    public Double checkAvailabilityAndGetPrice(Accommodation accommodation, Date date, Double priceMin, Double priceMax, int peopleNum){
        ReservationDate reservationDate = reservationDateRepository.findBy(accommodation.getId(), date, priceMin, priceMax, peopleNum);

        if (reservationDate == null){
            //goes here if reservationDate doesn't exist for date
            if(priceMin != null && priceMax != null){
                if(isPriceInRange(accommodation, priceMin,priceMax, peopleNum)){
                    return accommodation.getDefaultPrice();
                }else{
                    return null;
                }
            }
            return accommodation.getDefaultPrice();
        }
        if(reservationDate.getReservation() == null) {
            return reservationDate.getPrice();
        }
        return null;
    }
    public boolean isPriceInRange(Accommodation accommodation, Double priceMin, Double priceMax, int peopleNum){
        Double accomodationPrice = accommodation.getDefaultPrice();
        if(accommodation.getPriceType() == PriceType.FOR_ACCOMMODATION){
            accomodationPrice = accomodationPrice/peopleNum;
        }
        if(accomodationPrice<=priceMax && accomodationPrice>=priceMin){
            return true;
        }else{
            return false;
        }
    }

    public SearchAccommodationDTO checkAvailability(Accommodation accommodation, DateRange dateRange, Double priceMin, Double priceMax, int peopleNum){
        Calendar c = Calendar.getInstance();
        c.setTime(dateRange.getStartDate());
        Double totalPrice = 0.0;
        int nights = 0;
        for(; c.getTime().before(dateRange.getFinishDate()); c.add(Calendar.DATE, 1)){
            Double price = this.checkAvailabilityAndGetPrice(accommodation, c.getTime(), priceMin, priceMax, peopleNum);
            if (price == null){
                return null;
            }
            nights++;
            totalPrice += price;
        }
        if(accommodation.getPriceType() == PriceType.FOR_GUEST){
            totalPrice *= peopleNum;
        }
        return convertToSearchDto(accommodation, nights, totalPrice);
    }
    private SearchAccommodationDTO convertToSearchDto(Accommodation accommodation, int nights, Double totalPrice) {
        SearchAccommodationDTO accommodationDTO = modelMapper.map(accommodation, SearchAccommodationDTO.class);
        accommodationDTO.setAverageRating();
        accommodationDTO.setTotalPrice(totalPrice);
        accommodationDTO.setPricePerNight(nights);
        return accommodationDTO;
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
