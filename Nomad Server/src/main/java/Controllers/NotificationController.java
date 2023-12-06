package Controllers;

import DTO.AccommodationDTO;
import DTO.NotificationDTO;
import Services.IService;
import Services.NotificationService;
import model.Accommodation;
import model.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/notifications")
@ComponentScan(basePackageClasses = IService.class)
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<NotificationDTO>> getNotifications() {
        Collection<Notification> notifications = notificationService.findAll();
        Collection<NotificationDTO> notificationDTOS = notifications.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<NotificationDTO>>(notificationDTOS, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationDTO> getNotification(@PathVariable("id") Long id) {
        Notification notification = notificationService.findOne(id);

        if (notification == null) {
            return new ResponseEntity<NotificationDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<NotificationDTO>(this.convertToDto(notification), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) throws Exception {
        Notification notification = this.convertToEntity(notificationDTO);
        System.out.println(notification.getTargetUser().getUsername());
        notificationService.create(notification);
        return new ResponseEntity<NotificationDTO>(notificationDTO, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NotificationDTO> deleteNotification(@PathVariable("id") Long id) {
        notificationService.delete(id);
        return new ResponseEntity<NotificationDTO>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationDTO> updateNotification(@RequestBody NotificationDTO notificationDTO, @PathVariable Long id)
            throws Exception {
        Notification notificationForUpdate = notificationService.findOne(id);
        Notification updatedNotification = this.convertToEntity(notificationDTO);
        notificationForUpdate.copyValues(updatedNotification);

        notificationService.update(notificationForUpdate);

        if (updatedNotification == null) {
            return new ResponseEntity<NotificationDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<NotificationDTO>(notificationDTO, HttpStatus.OK);
    }

    private NotificationDTO convertToDto(Notification notification) {
        NotificationDTO notificationDTO = modelMapper.map(notification, NotificationDTO.class);

        return notificationDTO;
    }
    private Notification convertToEntity(NotificationDTO notificationDTO) {
        return modelMapper.map(notificationDTO, Notification.class);
    }
}
