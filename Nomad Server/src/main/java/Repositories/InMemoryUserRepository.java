package Repositories;

import model.UserReport;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class InMemoryUserReportRepository implements IRepository<UserReport, Long> {
    private ConcurrentMap<Long, UserReport> userReports;
    private static Long id = 0l;
    @Override
    public Collection<UserReport> findAll() {
        return userReports.values();
    }

    @Override
    public void create(UserReport object) {
        this.userReports.put(id, object);
        object.setId(id++);
    }

    @Override
    public UserReport findOne(Long id) {
        return this.userReports.get(id);
    }

    @Override
    public void update(UserReport object) {
        this.userReports.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.userReports.remove(id);
    }
}
