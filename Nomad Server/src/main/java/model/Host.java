package model;

import jakarta.persistence.*;
import model.enums.UserType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("host")
public class Host extends AppUser{

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "host")
    List<Accommodation> accommodations;
    @Override
    public Collection<UserType> getAuthorities() {
        return Collections.singletonList(UserType.HOST);
    }
    public Host(){
        super();
    };
}
