package app.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginSession {
    public final String email;
    public final String sessionToken;

    LoginSession(LoginSession other) {
        this.email = other.email;
        this.sessionToken = other.sessionToken;
    }
}
