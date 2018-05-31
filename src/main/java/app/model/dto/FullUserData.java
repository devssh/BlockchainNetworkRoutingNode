package app.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FullUserData {
    public static final String FULL = "full";
    public static final String VIEW = "dontcreate";
    public static final String REDEEM = "redeem";

    public final String email;
    public final String password;
    public final String sessionToken;
    public final String validUntil;
    public final String authorizationLevel;

    public FullUserData(FullUserData other) {
        this.email = other.email;
        this.password = other.password;
        this.sessionToken = other.sessionToken;
        this.validUntil = other.validUntil;
        this.authorizationLevel = other.authorizationLevel;
    }
}
