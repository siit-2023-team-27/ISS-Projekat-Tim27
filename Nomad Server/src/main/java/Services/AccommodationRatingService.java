package Services;

import Repositories.AccommodationRatingRepository;
import Repositories.AccommodationRepository;
import Repositories.IRepository;
import Repositories.UserRepository;
import model.Accommodation;
import model.AccommodationRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class AccommodationRatingService implements IService<AccommodationRating, Long> {

    @Autowired
    private AccommodationRatingRepository accommodationRatingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccommodationRepository accommodationRepository;

    @Override
    public Collection<AccommodationRating> findAll() {
        return accommodationRatingRepository.findAll();
    }

    @Override
    public void create(AccommodationRating object) {
        object.setUser(userRepository.findOneById(object.getUserId()));
        object.setAccommodation(accommodationRepository.findOneById(object.getAccommodationId()));
        accommodationRatingRepository.save(object);
    }

    @Override
    public AccommodationRating findOne(Long id) {
        return accommodationRatingRepository.findOneById(id);
    }

    @Override
    public void update(AccommodationRating object) {
        accommodationRatingRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        accommodationRatingRepository.deleteById(id);
    }

    public Collection<AccommodationRating> findRatingsForAccommodation(Long id) {
        return accommodationRatingRepository.findAllByAccommodation_Id(id);
    }
}
