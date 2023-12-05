package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("host")
public class Host extends AppUser{

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "host")
    List<Accommodation> accommodations;
}
