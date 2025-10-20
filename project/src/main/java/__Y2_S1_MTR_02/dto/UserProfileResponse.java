package __Y2_S1_MTR_02.dto;

import jakarta.validation.constraints.*;

public class UserProfileResponse {

    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 60, message = "Full name must be between 3 and 60 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Full name can only contain letters and spaces"
    )
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(?:\\+?\\d{10,15})$",
            message = "Phone number must contain 10–15 digits and may include a country code"
    )
    private String phone;


    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String fullName, String email, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
