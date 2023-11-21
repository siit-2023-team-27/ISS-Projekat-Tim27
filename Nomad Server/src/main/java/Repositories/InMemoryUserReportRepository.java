package Repositories;

import model.Accommodation;
import model.CommentReport;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class InMemoryCommentReportRepository implements IRepository<CommentReport, Long> {
    private ConcurrentMap<Long, CommentReport> commentReports;
    private static Long id = 0l;
    @Override
    public Collection<CommentReport> findAll() {
        return commentReports.values();
    }

    @Override
    public void create(CommentReport object) {
        this.commentReports.put(id, object);
        object.setId(id++);
    }

    @Override
    public CommentReport findOne(Long id) {
        return this.commentReports.get(id);
    }

    @Override
    public void update(CommentReport object) {
        this.commentReports.replace(object.getId(), object);
    }

    @Override
    public void delete(Long id) {
        this.commentReports.remove(id);
    }
}
