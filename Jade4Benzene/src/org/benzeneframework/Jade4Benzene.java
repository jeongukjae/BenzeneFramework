package org.benzeneframework;

import de.neuland.jade4j.Jade4J;
import org.benzeneframework.library.Renderer;

import java.io.IOException;
import java.util.Map;

/**
 * Created by parkjuchan on 15. 11. 25..
 * @author parkjuchan
 *
 * render with jade engine
 */
public class Jade4Benzene implements Renderer {

    @Override
    public String render(String path, Map<String, ?> map) {
        String filePath = DocumentUtil.getFilePath(path);
        String result = null;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> tmp = (Map<String, Object>) map;
            result = Jade4J.render(filePath, tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getType() {
        return "view engine";
    }
}
