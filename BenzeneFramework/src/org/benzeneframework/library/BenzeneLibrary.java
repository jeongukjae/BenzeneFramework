package org.benzeneframework.library;

import org.benzeneframework.Benzene;

/**
 * Created by jeongukjae on 15. 11. 20..
 * @author jeongukjae
 *
 * This class will help you writing benzene library class properly.
 */
public interface BenzeneLibrary {
    /**
     *
     * @return Type. Type will be used for {link org.org.benzeneframework.middleware.Preferences} by @{@link Benzene}.
     */
    String getType();
}
