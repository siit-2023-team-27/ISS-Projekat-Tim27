package Services;

import Repositories.IRepository;
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
    private IRepository<AccommodationRating, Long> accommodationRatingRepository;

    @Override
    public Collection<AccommodationRating> findAll() {
        return accommodationRatingRepository.findAll();
    }

    @Override
    public void create(AccommodationRating object) {
        accommodationRatingRepository.create(object);
    }

    @Override
    public AccommodationRating findOne(Long id) {
        return accommodationRatingRepository.findOne(id);
    }

    @Override
    public void update(AccommodationRating object) {
        accommodationRatingRepository.update(object);
    }

    @Override
    public void delete(Long id) {
        accommodationRatingRepository.delete(id);
    }
}
