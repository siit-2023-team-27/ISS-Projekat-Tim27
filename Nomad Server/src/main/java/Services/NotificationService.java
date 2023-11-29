package Services;

import Repositories.IRepository;
import model.Notification;
import model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
@Service
public class NotificationService implements IService<Notification, Long> {
    @Autowired
    private IRepository<Notification, Long> notificationRepository;
    @Override
    public Collection<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public void create(Notification object) {
        notificationRepository.create(object);
    }

    @Override
    public Notification findOne(Long id) {
        return notificationRepository.findOne(id);
    }

    @Override
    public void update(Notification object) {
        notificationRepository.update(object);
    }

    @Override
    public void delete(Long id) {
        notificationRepository.delete(id);
    }
}
