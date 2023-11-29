package Repositories;

import model.Accommodation;
import model.HostComment;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryHostCommentsRepository implements IRepository<HostComment, Long> {

    private ConcurrentMap<Long, HostComment> hostComments = new ConcurrentHashMap<Long, HostComment>();
    private static Long id = 0l;

    @Override
    public Collection<HostComment> findAll() {
        return this.hostComments.values();
    }

    @Override
    public void create(HostComment object) {
        this.hostComments.put(id, object);
        object.setId(id++);
    }

    @Override
    public HostComment findOne(Long id) {
        return this.hostComments.get(id);
    }

    @Override
    public void update(HostComment object) {
        this.hostComments.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.hostComments.remove(id);
    }
}
