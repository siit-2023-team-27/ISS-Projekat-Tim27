package Services;

import Repositories.InMemoryAccommodationRepository;
import model.Accommodation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AccommodationService implements IService<Accommodation, Long> {

    @Autowired
    private InMemoryAccommodationRepository accommodationRepository;

    @Override
    public Collection<Accommodation> findAll() {
        Collection<Accommodation> accommodations = accommodationRepository.findAll();
        return accommodations;
    }

    @Override
    public Accommodation findOne(Long id) {
        Accommodation accommodation = accommodationRepository.findOne(id);
        return accommodation;
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
