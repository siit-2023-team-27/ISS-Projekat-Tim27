package Services;

import Repositories.HostCommentRepository;
import Repositories.IRepository;
import model.Accommodation;
import model.HostComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@ComponentScan(basePackageClasses = IRepository.class)
public class HostCommentService implements IService<HostComment, Long> {

    @Autowired
    private HostCommentRepository hostCommentRepository;

    @Override
    public Collection<HostComment> findAll() {
        return hostCommentRepository.findAll();
    }

    @Override
    public void create(HostComment object) {
        hostCommentRepository.save(object);
    }

    @Override
    public HostComment findOne(Long id) {
        return hostCommentRepository.findOneById(id);
    }

    @Override
    public void update(HostComment object) {
        hostCommentRepository.save(object);
    }

    @Override
    public void delete(Long id) {
        hostCommentRepository.deleteById(id);
    }
}
