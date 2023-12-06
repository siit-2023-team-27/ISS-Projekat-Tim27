package Services;

import Repositories.AmenityRepository;
import Repositories.IRepository;
import model.Amenity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan("Repositories")
public class AmenityService implements IService<Amenity, Long>{

    @Autowired
    private AmenityRepository amenityRepository;

    @Override
    public Collection<Amenity> findAll() { return amenityRepository.findAll(); }

    @Override
    public void create(Amenity object) { amenityRepository.save(object); }

    @Override
    public Amenity findOne(Long id) {return amenityRepository.findOneById(id); }

    @Override
    public void update(Amenity object) { amenityRepository.save(object); }

    @Override
    public void delete(Long id) { amenityRepository.deleteById(id); }
}
