package Services;

import Repositories.IRepository;
import model.Accommodation;
import model.HostRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class HostRatingService implements IService<HostRating, Long> {


    @Autowired
    private IRepository<HostRating, Long> hostRatingRepository;

    @Override
    public Collection<HostRating> findAll() {
        return hostRatingRepository.findAll();
    }

    @Override
    public void create(HostRating object) {
        hostRatingRepository.create(object);
    }

    @Override
    public HostRating findOne(Long id) {
        return hostRatingRepository.findOne(id);
    }

    @Override
    public void update(HostRating object) {
        hostRatingRepository.update(object);
    }

    @Override
    public void delete(Long id) {
        hostRatingRepository.delete(id);
    }
}
