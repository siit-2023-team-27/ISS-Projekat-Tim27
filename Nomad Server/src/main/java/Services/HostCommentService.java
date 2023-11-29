package Services;

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
    private IRepository<HostComment, Long> hostCommentRepository;

    @Override
    public Collection<HostComment> findAll() {
        return hostCommentRepository.findAll();
    }

    @Override
    public void create(HostComment object) {
        hostCommentRepository.create(object);
    }

    @Override
    public HostComment findOne(Long id) {
        return hostCommentRepository.findOne(id);
    }

    @Override
    public void update(HostComment object) {
        hostCommentRepository.update(object);
    }

    @Override
    public void delete(Long id) {
        hostCommentRepository.delete(id);
    }
}
