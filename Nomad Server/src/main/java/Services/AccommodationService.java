package Services;

import Repositories.IRepository;
import Repositories.InMemoryAccommodationRepository;
import model.Accommodation;
import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class AccommodationService implements IService<Accommodation, Long> {

    @Autowired
    private IRepository<Accommodation, Long> accommodationRepository;

    @Override
    public Collection<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Accommodation findOne(Long id) {
        return accommodationRepository.findOne(id);
    }

    @Override
    public void create(Accommodation accommodation) {
        accommodationRepository.create(accommodation);
    }

    @Override
    public void update(Accommodation accommodation) {
        accommodationRepository.update(accommodation);
    }

    @Override
    public void delete(Long id) {
        accommodationRepository.delete(id);
    }

}
