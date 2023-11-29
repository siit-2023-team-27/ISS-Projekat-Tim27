package Controllers;

import DTO.CommentDTO;
import DTO.CommentReportDTO;
import Services.IService;
import model.Comment;
import model.CommentReport;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/comment-reports")
@ComponentScan(basePackageClasses = IService.class)
public class CommentReportController {
    @Autowired
    private IService<CommentReport, Long> commentService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CommentReportDTO>> getUsers() {
        Collection<CommentReport> comments = commentService.findAll();
        Collection<CommentReportDTO> commentsDTOS = comments.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<CommentReportDTO>>(commentsDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> getUser(@PathVariable("id") Long id) {
        CommentReport comment = commentService.findOne(id);

        if (comment == null) {
            return new ResponseEntity<CommentReportDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<CommentReportDTO>(this.convertToDto(comment), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> createAccommodation(@RequestBody CommentReportDTO commentDTO) throws Exception {
        CommentReport comment = this.convertToEntity(commentDTO);
        commentService.create(comment);
        return new ResponseEntity<CommentReportDTO>(commentDTO, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommentReportDTO> deleteAccommodation(@PathVariable("id") Long id) {
        commentService.delete(id);
        return new ResponseEntity<CommentReportDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentReportDTO> updateAccommodation(@RequestBody CommentReportDTO commentDTO, @PathVariable Long id)
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
}
