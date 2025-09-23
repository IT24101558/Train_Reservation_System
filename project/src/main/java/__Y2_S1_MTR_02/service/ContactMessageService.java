package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.ContactMessageDTO;
import __Y2_S1_MTR_02.model.ContactMessage;
import __Y2_S1_MTR_02.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactMessageService {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ContactMessageDTO saveMessage(ContactMessageDTO contactMessageDTO) {
        ContactMessage contactMessage = convertToEntity(contactMessageDTO);
        contactMessage.setDateTime(LocalDateTime.now());
        ContactMessage savedMessage = contactMessageRepository.save(contactMessage);
        return convertToDTO(savedMessage);
    }

    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }

    private ContactMessageDTO convertToDTO(ContactMessage contactMessage) {
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setId(contactMessage.getId());
        dto.setName(contactMessage.getName());
        dto.setEmail(contactMessage.getEmail());
        dto.setSubject(contactMessage.getSubject());
        dto.setMessage(contactMessage.getMessage());
        dto.setDateTime(contactMessage.getDateTime());
        return dto;
    }

    private ContactMessage convertToEntity(ContactMessageDTO dto) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setId(dto.getId());
        contactMessage.setName(dto.getName());
        contactMessage.setEmail(dto.getEmail());
        contactMessage.setSubject(dto.getSubject());
        contactMessage.setMessage(dto.getMessage());
        return contactMessage;
    }
}
