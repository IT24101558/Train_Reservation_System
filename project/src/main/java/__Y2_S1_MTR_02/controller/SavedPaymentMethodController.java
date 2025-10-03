package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.model.SavedPaymentMethod;
import __Y2_S1_MTR_02.model.UserAccount;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import __Y2_S1_MTR_02.service.SavedPaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SavedPaymentMethodController {

    @Autowired
    private SavedPaymentMethodService savedPaymentMethodService;
    
    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("/payment-methods/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("API is working!");
    }

    @GetMapping("/payment-methods/{userId}")
    public ResponseEntity<List<SavedPaymentMethod>> getPaymentMethodsByUserId(@PathVariable Long userId) {
        List<SavedPaymentMethod> methods = savedPaymentMethodService.getPaymentMethodsByUserId(userId);
        return ResponseEntity.ok(methods);
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<?> addPaymentMethod(@RequestBody SavedPaymentMethod paymentMethod) {
        try {
            System.out.println("Received payment method: " + paymentMethod);
            
            // Fetch the full UserAccount entity
            if (paymentMethod.getUser() != null && paymentMethod.getUser().getId() != null) {
                Optional<UserAccount> userOpt = userAccountRepository.findById(paymentMethod.getUser().getId());
                if (userOpt.isPresent()) {
                    UserAccount user = userOpt.get();
                    paymentMethod.setUser(user);
                    paymentMethod.setOwnerEmail(user.getEmail()); // Set the owner email from user account
                    System.out.println("User found: " + user.getFullName() + " (" + user.getEmail() + ")");
                } else {
                    System.out.println("User not found with ID: " + paymentMethod.getUser().getId());
                    return ResponseEntity.badRequest().body("User not found with ID: " + paymentMethod.getUser().getId());
                }
            } else {
                System.out.println("No user ID provided");
                return ResponseEntity.badRequest().body("User ID is required");
            }
            
            SavedPaymentMethod savedMethod = savedPaymentMethodService.addPaymentMethod(paymentMethod);
            System.out.println("Payment method saved successfully: " + savedMethod.getId());
            return ResponseEntity.ok(savedMethod);
        } catch (Exception e) {
            System.err.println("Error saving payment method: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error saving payment method: " + e.getMessage());
        }
    }

    @PutMapping("/payment-methods/{id}")
    public ResponseEntity<SavedPaymentMethod> updatePaymentMethod(
            @PathVariable Long id, 
            @RequestBody SavedPaymentMethod paymentMethod) {
        SavedPaymentMethod updatedMethod = savedPaymentMethodService.updatePaymentMethod(id, paymentMethod);
        if (updatedMethod != null) {
            return ResponseEntity.ok(updatedMethod);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/payment-methods/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        savedPaymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.ok().build();
    }
}
