package app.model.view;

import app.model.dto.FullUserData;
import lombok.AllArgsConstructor;

import java.util.concurrent.ConcurrentMap;

@AllArgsConstructor
public class UsersView {
    public final ConcurrentMap<String, FullUserData> users;
}
