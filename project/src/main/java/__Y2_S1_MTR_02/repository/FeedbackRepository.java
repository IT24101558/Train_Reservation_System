package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEmailIgnoreCase(String email);
}
