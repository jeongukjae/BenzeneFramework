package org.benzeneframework.library;

import java.util.Map;

/**
 * Created by jeongukjae on 15. 11. 21..
 *
 * This class will help you writing rendering class.
 */
public interface Renderer extends BenzeneLibrary {

    /**
     *
     * @param path File path Before Rendering
     * @return Rendered Content
     */
    String render(String path, Map<String, ?> params);
}
