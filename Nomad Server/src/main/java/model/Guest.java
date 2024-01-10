package model;

import jakarta.persistence.*;
import model.enums.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("guest")
public class Guest extends AppUser{
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "guest")
    List<Reservation> reservations;

    Long cancellationNumber;

    public void increaseNumber(){
        this.cancellationNumber++;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public long getCancellationNumber() {
        return cancellationNumber;
    }

    public void setCancellationNumber(long cancellationNumber) {
        this.cancellationNumber = cancellationNumber;
    }

    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.GUEST);
    }
    public Guest(){
        super();
    };
}
