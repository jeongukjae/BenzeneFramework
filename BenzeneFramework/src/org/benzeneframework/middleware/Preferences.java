package org.benzeneframework.middleware;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeongukjae on 15. 11. 20..
 * @author jeongukjae
 *
 * This class will be saving and loading preferences.
 * Just call getInstance and call what you want.
 */
public class Preferences {
    private static Preferences preferences;
    private Map<String, Object> values;

    private Preferences() {
        values = new HashMap<>();
    }
    public static Preferences getInstance() {
        if(preferences == null) {
            preferences = new Preferences();
            return preferences;
        } else {
            return preferences;
        }
    }

    public Object get(String key) {
        return values.get(key);
    }

    public void put(String key, Object object) {
        values.put(key, object);
    }
}
