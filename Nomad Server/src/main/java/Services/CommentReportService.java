package Services;

import Repositories.IRepository;
import model.Comment;
import model.CommentReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CommentReportService implements IService<CommentReport, Long> {

    @Autowired
    private IRepository<CommentReport, Long> commentReportRepository;

    @Override
    public Collection<CommentReport> findAll() {
        return commentReportRepository.findAll();
    }

    @Override
    public CommentReport findOne(Long id) {
        return commentReportRepository.findOne(id);
    }

    @Override
    public void create(CommentReport commentReport) {
        commentReportRepository.create(commentReport);
    }

    @Override
    public void update(CommentReport commentReport) {
        commentReportRepository.update(commentReport);
    }

    @Override
    public void delete(Long id) {
        commentReportRepository.delete(id);
    }

}
