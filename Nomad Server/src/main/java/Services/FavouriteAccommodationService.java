package Services;

import Repositories.FavouriteAccommodationRepository;
import model.FavouriteAccommodation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

public class FavouriteAccommodationService implements IService<FavouriteAccommodation, Long>{

    @Autowired
    FavouriteAccommodationRepository favouriteRepository;
    @Override
    public Collection<FavouriteAccommodation> findAll() { return favouriteRepository.findAll(); }

    @Override
    public void create(FavouriteAccommodation object) { favouriteRepository.save(object); }

    @Override
    public FavouriteAccommodation findOne(Long id) { return favouriteRepository.findOneById(id); }

    @Override
    public void update(FavouriteAccommodation object) { favouriteRepository.save(object); }

    @Override
    public void delete(Long id) { favouriteRepository.deleteById(id); }
}
