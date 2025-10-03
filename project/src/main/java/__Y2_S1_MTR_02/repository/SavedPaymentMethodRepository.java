package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.SavedPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedPaymentMethodRepository extends JpaRepository<SavedPaymentMethod, Long> {
    List<SavedPaymentMethod> findByUser_Id(Long userId);
}
