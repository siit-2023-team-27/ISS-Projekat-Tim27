package Services;

import Repositories.IRepository;
import model.User;
import model.UserReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserReportService implements IService<UserReport, Long> {

    @Autowired
    private IRepository<UserReport, Long> userReportRepository;

    @Override
    public Collection<UserReport> findAll() {
        return userReportRepository.findAll();
    }

    @Override
    public UserReport findOne(Long id) {
        return userReportRepository.findOne(id);
    }

    @Override
    public void create(UserReport userReport) {
        userReportRepository.create(userReport);
    }

    @Override
    public void update(UserReport userReport) {
        userReportRepository.update(userReport);
    }

    @Override
    public void delete(Long id) {
        userReportRepository.delete(id);
    }

}
