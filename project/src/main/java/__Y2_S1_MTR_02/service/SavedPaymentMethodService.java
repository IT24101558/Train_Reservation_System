package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.model.SavedPaymentMethod;
import __Y2_S1_MTR_02.repository.SavedPaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedPaymentMethodService {

    @Autowired
    private SavedPaymentMethodRepository repository;

    public List<SavedPaymentMethod> getPaymentMethodsByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }

    public SavedPaymentMethod addPaymentMethod(SavedPaymentMethod paymentMethod) {
        return repository.save(paymentMethod);
    }

    public void deletePaymentMethod(Long id) {
        repository.deleteById(id);
    }

    public SavedPaymentMethod updatePaymentMethod(Long id, SavedPaymentMethod paymentMethod) {
        SavedPaymentMethod existingMethod = repository.findById(id).orElse(null);
        if (existingMethod != null) {
            existingMethod.setCardholderName(paymentMethod.getCardholderName());
            existingMethod.setCardNumber(paymentMethod.getCardNumber());
            existingMethod.setExpiryDate(paymentMethod.getExpiryDate());
            existingMethod.setCvv(paymentMethod.getCvv());
            return repository.save(existingMethod);
        }
        return null;
    }
}
