package Services;

import Repositories.HostRatingRepository;
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
    private HostRatingRepository hostRatingRepository;

    @Override
    public Collection<HostRating> findAll() {
        return hostRatingRepository.findAll();
    }

    @Override
    public void create(HostRating object) {
        hostRatingRepository.save(object);
    }

    @Override
    public HostRating findOne(Long id) {
        return hostRatingRepository.findOneById(id);
    }

    @Override
    public void update(HostRating object) {
        hostRatingRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        hostRatingRepository.deleteById(id);
    }

    public Collection<HostRating> findAllRatingsForHost(Long id) {
        return hostRatingRepository.findAllByHost_Id(id);
    }
}
