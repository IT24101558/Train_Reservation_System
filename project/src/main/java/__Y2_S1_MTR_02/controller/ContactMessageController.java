package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.ContactMessageDTO;
import __Y2_S1_MTR_02.service.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        return ResponseEntity.ok(contactMessageService.getAllMessages());
    }

    @PostMapping
    public ResponseEntity<ContactMessageDTO> saveMessage(@RequestBody ContactMessageDTO contactMessageDTO) {
        return ResponseEntity.ok(contactMessageService.saveMessage(contactMessageDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
