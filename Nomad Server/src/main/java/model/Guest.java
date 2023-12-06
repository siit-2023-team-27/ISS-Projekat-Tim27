package model;

import jakarta.persistence.*;
import model.enums.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("guest")
public class Guest extends AppUser{
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "guest")
    List<Reservation> reservations;
    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.GUEST);
    }
}
