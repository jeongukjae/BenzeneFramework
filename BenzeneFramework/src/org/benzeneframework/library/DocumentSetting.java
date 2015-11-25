package org.benzeneframework.library;

import org.benzeneframework.middleware.Preferences;

import java.io.File;

public class DocumentSetting {
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
