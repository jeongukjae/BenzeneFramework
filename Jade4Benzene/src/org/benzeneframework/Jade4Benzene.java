package org.benzeneframework;

import de.neuland.jade4j.Jade4J;
import org.benzeneframework.library.BenzeneLibrary;
import org.benzeneframework.library.BenzeneRenderer;
import org.benzeneframework.library.DocumentSetting;

import java.io.IOException;
import java.util.Map;

/**
 * Created by jeongukjae on 15. 11. 25..
 */
public class Jade4Benzene implements BenzeneLibrary, BenzeneRenderer{

    @Override
    public String render(String path, Map<String, ?> map) {
        String filePath = DocumentSetting.getFilePath(path);
        String result = null;
        try {
            result = Jade4J.render(filePath, (Map<String, Object>) map);
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
