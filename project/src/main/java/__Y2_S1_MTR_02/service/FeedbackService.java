package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.FeedbackDTO;
import __Y2_S1_MTR_02.model.Feedback;
import __Y2_S1_MTR_02.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FeedbackDTO> getFeedbacksByUserEmail(String email) {
        return feedbackRepository.findByEmailIgnoreCase(email).stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public FeedbackDTO saveFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = convertToEntity(feedbackDTO);
        feedback.setDateTime(LocalDateTime.now());
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToDTO(savedFeedback);
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setPassengerName(feedback.getPassengerName());
        dto.setEmail(feedback.getEmail());
        dto.setRating(feedback.getRating());
        dto.setMessage(feedback.getMessage());
        dto.setDateTime(feedback.getDateTime());
        return dto;
    }

    private Feedback convertToEntity(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setId(dto.getId());
        feedback.setPassengerName(dto.getPassengerName());
        feedback.setEmail(dto.getEmail());
        feedback.setRating(dto.getRating());
        feedback.setMessage(dto.getMessage());
        return feedback;
    }
}
