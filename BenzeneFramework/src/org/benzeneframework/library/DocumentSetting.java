package org.benzeneframework.library;

import org.benzeneframework.middleware.Preferences;

import java.io.File;

/**
 * Created by ParkJuChan on 15. 11. 25..
 * @author ParkJuChan, JeongUkJae
 *
 * This class will help you set/get document setting.
 * More functions will be appneded in future.
 */
public class DocumentSetting {

    /**
     * Get File Path
     *
     * @param path path in views path
     * @return file path. If this method returns null, the file doesn't exist.
     */
    @SuppressWarnings("unused")
	public static String getFilePath(String path) {
		Preferences p = Preferences.getInstance();
		String viewsPath = (String) p.get("view");
		File f;
		if (viewsPath == null) {
			f = new File(path);
		} else {
			if (viewsPath.indexOf(viewsPath.length() - 1) != '/') {
				viewsPath += '/';
			}
			f = new File(viewsPath + path);
			if (!f.exists())
				f = new File(path);
		}

		if (!f.exists()) {
			return null;
		}

		return f.getPath();
	}
}
