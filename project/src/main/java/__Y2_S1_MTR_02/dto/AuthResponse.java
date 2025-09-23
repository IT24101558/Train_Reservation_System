package __Y2_S1_MTR_02.dto;

public class AuthResponse {
    private boolean success;
    private String role;
    private String message;

    public AuthResponse() {}

    public AuthResponse(boolean success, String role, String message) {
        this.success = success;
        this.role = role;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


