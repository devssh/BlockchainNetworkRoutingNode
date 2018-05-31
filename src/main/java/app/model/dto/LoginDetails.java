package app.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginDetails {
    public final String email;
    public final String password;

    LoginDetails(LoginDetails other) {
        this.email = other.email;
        this.password = other.password;
    }

    @Override
    public String toString() {
        return "LoginDetails{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
