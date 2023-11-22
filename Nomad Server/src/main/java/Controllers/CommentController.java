package Controllers;

import DTO.CommentDTO;
import DTO.UserDTO;
import Services.IService;
import model.Comment;
import model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/comments")
@ComponentScan(basePackageClasses = IService.class)
public class CommentController {
    @Autowired
    private IService<Comment, Long> commentService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<CommentDTO>> getUsers() {
        Collection<Comment> comments = commentService.findAll();
        Collection<CommentDTO> commentsDTOS = comments.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<CommentDTO>>(commentsDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> getUser(@PathVariable("id") Long id) {
        Comment comment = commentService.findOne(id);

        if (comment == null) {
            return new ResponseEntity<CommentDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<CommentDTO>(this.convertToDto(comment), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> createAccommodation(@RequestBody CommentDTO commentDTO) throws Exception {
        Comment comment = this.convertToEntity(commentDTO);
        commentService.create(comment);
        return new ResponseEntity<CommentDTO>(commentDTO, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> deleteAccommodation(@PathVariable("id") Long id) {
        commentService.delete(id);
        return new ResponseEntity<CommentDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentDTO> updateAccommodation(@RequestBody CommentDTO commentDTO, @PathVariable Long id)
            throws Exception {
        Comment commentForUpdate = commentService.findOne(id);
        Comment updatedComment = this.convertToEntity(commentDTO);
        commentForUpdate.copyValues(updatedComment);

        commentService.update(commentForUpdate);

        if (updatedComment == null) {
            return new ResponseEntity<CommentDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<CommentDTO>(commentDTO, HttpStatus.OK);
    }

    private CommentDTO convertToDto(Comment comment) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);

        return commentDTO;
    }
    private Comment convertToEntity(CommentDTO commentDTO) {
        return modelMapper.map(commentDTO, Comment.class);
    }
}
