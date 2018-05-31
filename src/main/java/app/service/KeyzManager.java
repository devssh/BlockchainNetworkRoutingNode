package app.service;

import app.model.Keyz;
import app.utils.FileUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import static app.model.Keyz.GenerateKeyWithName;
import static app.utils.JsonUtils.FromJSON;
import static app.utils.JsonUtils.ToJSON;

public class KeyzManager {

    public static final ConcurrentMap<String, ConcurrentLinkedQueue<Keyz>> KEYS = new ConcurrentHashMap<>();

    public static String GenerateAndStoreRandomKey(String name) {
        String dev = ToJSON(GenerateKeyWithName(name));
        FileUtil.StoreKey(dev);
        return dev;
    }

    public static String CreateAndStoreKey(String name) {
        Keyz keyz = GenerateKeyWithName(name);
        String keyString = CreateKey(ToJSON(keyz));
        FileUtil.StoreKey(keyString);
        return keyString;
    }
    public static String CreateKey(String keyJSON) {
        final Keyz keyz = FromJSON(keyJSON, Keyz.class);
        if (!KEYS.containsKey(keyz.owner)) {
            KEYS.put(keyz.owner, new ConcurrentLinkedQueue<Keyz>() {{
                add(keyz);
            }});
        } else {
            ConcurrentLinkedQueue<Keyz> keyzIntegerConcurrentHashMap = KEYS.get(keyz.owner);
            keyzIntegerConcurrentHashMap.add(keyz);
        }

        return ToJSON(keyz);
    }
}
