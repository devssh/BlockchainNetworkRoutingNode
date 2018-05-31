package app.service;

import app.model.dto.Activation;
import app.model.dto.FullUserData;
import app.model.dto.LoginDetails;
import app.model.dto.LoginSession;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static app.model.Keyz.GenerateSeed;
import static app.model.dto.FullUserData.FULL;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.RegistrationManager.RegistrationCodes;
import static app.service.RegistrationManager.RegistrationPendingUsers;
import static app.utils.FileUtil.StoreUser;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;

public class UserManager {
    public static final ConcurrentMap<String, FullUserData> Users = new ConcurrentHashMap<>();

    public static LoginSession CreateUser(String userJSON) {
        FullUserData fullUserData = FromJSON(userJSON, FullUserData.class);
        RegistrationPendingUsers.remove(fullUserData.email);
        RegistrationCodes.remove(fullUserData.email);
        Users.putIfAbsent(fullUserData.email, fullUserData);
        CreateAndStoreKey(fullUserData.email);
        return new LoginSession(fullUserData.email, fullUserData.sessionToken);
    }

    public static LoginSession CreateAndStoreUserIfActivated(Activation activation) {
        LoginDetails loginDetails = RegistrationPendingUsers.get(activation.email);
        if (activation.email.equals(loginDetails.email) &&
                activation.activationCode.equals(RegistrationCodes.get(activation.email))) {

            FullUserData user = new FullUserData(activation.email, loginDetails.password,
                    GenerateSeed(7), LocalDateTime.now().plusDays(1).toString(), FULL);
            StoreUser(user);
            //TODO: Delete from the file after creation of User without restart
            return CreateUser(ToJSON(user));
        }
        throw new NullPointerException();
    }

    public static boolean isValidSession(LoginSession loginSession, String[] authorizationLevel) {
        FullUserData fullUserData = Users.get(loginSession.email);
        if (fullUserData != null && fullUserData.email.equals(loginSession.email) &&
                fullUserData.sessionToken.equals(loginSession.sessionToken) &&
                Arrays.asList(authorizationLevel).contains(fullUserData.authorizationLevel)) {
            return true;
        }
        return false;
    }
}
