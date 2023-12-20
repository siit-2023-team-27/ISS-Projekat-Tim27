package Services;

import DTO.ReservationDTO;
import Repositories.IRepository;
import Repositories.ReservationDateRepository;
import Repositories.ReservationRepository;
import jakarta.transaction.Transactional;
import model.Comment;
import model.Reservation;
import model.ReservationDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class ReservationService implements IService<Reservation, Long> {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private ReservationDateRepository reservationDateRepository;

    @Override
    public Collection<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findOne(Long id) {
        return reservationRepository.findOneById(id);
    }

    @Override
    public void create(Reservation reservation) {
        reservationRepository.save(reservation);
    }
    public void createReservationDates(Reservation reservation) {
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getDateRange().getStartDate());
        for(; c.getTime().before(reservation.getDateRange().getFinishDate()); c.add(Calendar.DATE, 1)){
            this.createReservationDate(new ReservationDate(reservation.getAccommodation(), reservation, reservation.getAccommodation().getDefaultPrice(), c.getTime()));
        }
    }
    public Collection<Reservation> findReservationsForUser(long userId){
        return reservationRepository.findAllByAccommodation_Host_id(userId);
    }
    public Collection<Reservation> findReservationsForGuest(long userId){
        return reservationRepository.findAllByGuest_id(userId);
    }
    private void createReservationDate(ReservationDate reservationDate) {
        ReservationDate reservationDateToUpdate = reservationDateRepository.findOneByAccommodation_IdAndDate(reservationDate.getAccommodation().getId(), reservationDate.getDate());
        if(reservationDateToUpdate == null){
            reservationDateRepository.save(reservationDate);
            return;
        }
        reservationDateToUpdate.setReservation(reservationDate.getReservation());
        reservationDateRepository.save(reservationDateToUpdate);
    }

    public boolean reserve(Reservation reservation){
        if(!this.isAvailable(reservation)){
            return false;
        }
        this.create(reservation);
        this.createReservationDates(reservation);
        return true;
    }
    public boolean isAvailable(Reservation reservation){
        Calendar c = Calendar.getInstance();
        c.setTime(reservation.getDateRange().getStartDate());
        for(; c.getTime().before(reservation.getDateRange().getFinishDate()); c.add(Calendar.DATE, 1)){
            if (!accommodationService.isAvailable(reservation.getAccommodation().getId(), c.getTime()) ){
                return false;
            }
        }
        return true;
    }
    @Override
    public void update(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        reservationDateRepository.deleteByReservation_id(id);

        reservationRepository.deleteById(id);
    }
    public boolean validateReservation(Reservation reservation){
        if(reservation.getAccommodation() == null){
            return false;
        }
        if(reservation.getNumGuests() < reservation.getAccommodation().getMinGuests()){
            return false;
        }
        if(reservation.getNumGuests() > reservation.getAccommodation().getMaxGuests()){
            return false;
        }
        if(reservation.getUser() == null){
            return false;
        }
        return true;
    }

}
