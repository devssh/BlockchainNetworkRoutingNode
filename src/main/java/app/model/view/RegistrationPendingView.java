package app.model.view;

import app.model.dto.LoginDetails;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class RegistrationPendingView {
    public final ConcurrentMap<String, LoginDetails> registrationPending;
}
