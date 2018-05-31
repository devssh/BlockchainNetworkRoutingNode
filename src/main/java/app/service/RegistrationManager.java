package app.service;

import app.model.dto.Activation;
import app.model.dto.LoginDetails;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.Keyz.GenerateSeed;
import static app.service.UserManager.Users;
import static app.utils.FileUtil.StoreRegistration;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;

public class RegistrationManager {
    public static final ConcurrentMap<String, LoginDetails> RegistrationPendingUsers = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, String> RegistrationCodes = new ConcurrentHashMap<>();

    public static LoginDetails CreateRegistration(String regJson, String regCodeJson) {
        LoginDetails loginDetails = FromJSON(regJson, LoginDetails.class);
        Activation activationCode = FromJSON(regCodeJson, Activation.class);
        if (Users.get(loginDetails.email) == null) {
            RegistrationPendingUsers.putIfAbsent(loginDetails.email, loginDetails);
            RegistrationCodes.putIfAbsent(activationCode.email, activationCode.activationCode);
            return loginDetails;
        }
        throw new IllegalArgumentException();
    }

    public static void CreateAndStoreRegistration(LoginDetails loginDetails) {
        Activation activation = StoreRegistration(loginDetails, GenerateSeed(7));
        CreateRegistration(ToJSON(loginDetails), ToJSON(activation));
    }
}
