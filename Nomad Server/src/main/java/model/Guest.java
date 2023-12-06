package model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import model.enums.UserType;

import java.util.Collection;
import java.util.Collections;

@Entity
@DiscriminatorValue("guest")
public class Guest extends AppUser{
    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.GUEST);
    }
}
