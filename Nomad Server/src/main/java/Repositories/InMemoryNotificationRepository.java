package Repositories;

import model.Comment;
import model.Notification;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Repository
public class InMemoryNotificationRepository implements  IRepository<Notification, Long> {
    private ConcurrentMap<Long, Notification> notifications = new ConcurrentHashMap<Long, Notification>();
    private static Long id = 0l;
    @Override
    public Collection<Notification> findAll() {
        return notifications.values();
    }

    @Override
    public void create(Notification object) {
        this.notifications.put(id, object);
        object.setId(id++);
    }

    @Override
    public Notification findOne(Long id) {
        return this.notifications.get(id);
    }

    @Override
    public void update(Notification object) {
        this.notifications.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.notifications.remove(id);
    }
}
