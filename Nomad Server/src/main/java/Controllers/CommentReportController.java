package Controllers;

import DTO.AddCommentReportDTO;
import DTO.CommentDTO;
import DTO.CommentReportDTO;
import Services.CommentService;
import Services.IService;
import Services.UserService;
import model.Comment;
import model.CommentReport;
import model.enums.ReportStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(
        origins = {
                "http://localhost:4200"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })
@RestController
@RequestMapping("/api/comment-reports")
@ComponentScan(basePackageClasses = IService.class)
public class CommentReportController {
    @Autowired
    private IService<CommentReport, Long> commentService;
    @Autowired
    private CommentService comService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<AddCommentReportDTO>> getReports() {
        Collection<CommentReport> comments = commentService.findAll();
        Collection<AddCommentReportDTO> commentsDTOS = comments.stream().map(this::convertAddToDTO).toList();
        return new ResponseEntity<Collection<AddCommentReportDTO>>(commentsDTOS, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> getReport(@PathVariable("id") Long id) {
        CommentReport comment = commentService.findOne(id);

        if (comment == null) {
            return new ResponseEntity<CommentReportDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<CommentReportDTO>(this.convertToDto(comment), HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('HOST')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddCommentReportDTO> createReport(@RequestBody AddCommentReportDTO commentDTO) throws Exception {
        CommentReport comment = this.convertAddToEntity(commentDTO);
        commentService.create(comment);
        return new ResponseEntity<AddCommentReportDTO>(commentDTO, HttpStatus.CREATED);
    }



    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommentReportDTO> deleteReport(@PathVariable("id") Long id) {
        commentService.delete(id);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "accept/{id}")
    public ResponseEntity<CommentReportDTO> acceptReport(@PathVariable("id") Long id) {
        CommentReport report = commentService.findOne(id);
        report.setReportStatus(ReportStatus.ACCEPTED);
        commentService.update(report);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "archive/{id}")
    public ResponseEntity<CommentReportDTO> archiveReport(@PathVariable("id") Long id) {
        CommentReport report = commentService.findOne(id);
        report.setReportStatus(ReportStatus.ARCHIVED);
        commentService.update(report);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> updateReport(@RequestBody CommentReportDTO commentDTO, @PathVariable Long id)
            throws Exception {
        CommentReport commentForUpdate = commentService.findOne(id);
        CommentReport updatedComment = this.convertToEntity(commentDTO);
        commentForUpdate.copyValues(updatedComment);

        commentService.update(commentForUpdate);

        if (updatedComment == null) {
            return new ResponseEntity<CommentReportDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CommentReportDTO>(commentDTO, HttpStatus.OK);
    }

    private CommentReportDTO convertToDto(CommentReport comment) {
        CommentReportDTO commentDTO = modelMapper.map(comment, CommentReportDTO.class);

        return commentDTO;
    }
    private CommentReport convertToEntity(CommentReportDTO commentDTO) {
        return modelMapper.map(commentDTO, CommentReport.class);
    }
    private CommentReport convertAddToEntity(AddCommentReportDTO commentDTO) {
        CommentReport commentReport = new CommentReport();
        commentReport.setReportedComment(comService.findOne(commentDTO.getReportedComment()));
        commentReport.setReportingUser(userService.findOne(commentDTO.getReportingAppUser()));
        commentReport.setReportStatus(ReportStatus.PENDING);
        commentReport.setReason(commentDTO.getReason());
        return commentReport;
    }
    private AddCommentReportDTO convertAddToDTO(CommentReport report){
        AddCommentReportDTO dto = new AddCommentReportDTO();
        dto.setReason(report.getReason());
        dto.setReportingAppUser(report.getReportingUser().getId());
        dto.setReportedComment(report.getReportedComment().getId());
        dto.setReportStatus(report.getReportStatus());
        return dto;
    }
}
