package org.benzeneframework.middleware;

import org.benzeneframework.library.BenzeneLibrary;
import org.benzeneframework.library.BenzeneRenderer;
import org.benzeneframework.library.DocumentSetting;
import org.benzeneframework.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jeongukjae on 15. 11. 20..
 */
public class DefaultRenderer implements BenzeneLibrary, BenzeneRenderer {
    private static final String TAG = "DefaultRenderer";

    @Override
    public String getType() {
        return "view engine";
    }

    @Override
    public String render(String path, Map<String, ?> params) {
        String realPath = DocumentSetting.getFilePath(path);
        if(realPath == null) {
            Log.e(TAG, "Cannot get file path");
            return null;
        }
        File f = new File(realPath);

        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String tmp;
            while ((tmp = br.readLine()) != null) {
                result += tmp;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(result);

        StringBuffer stringBuffer = new StringBuffer();
        while(matcher.find()) {
            String text = matcher.group(1);
            String[] paramsArray = text.split(",");
            String paramResult = null;
            Object o = params.get(paramsArray[0]);

            if(paramsArray.length > 1) {
                Object tmp = o;
                for (int i = 1; i < paramsArray.length; i++) {
                    if (tmp instanceof Map) {
                        tmp = ((Map) tmp).get(paramsArray[i]);
                    } else {

                        Class tmpClass = tmp.getClass();
                        try {
                            tmp = tmpClass.getField(paramsArray[i]).get(tmp);
                        } catch (IllegalAccessException|NoSuchFieldException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    paramResult = tmp.toString();
                }
            } else {
                paramResult = (String)o;
            }
            if(paramResult == null) {
                paramResult = " ";
            }
            matcher.appendReplacement(stringBuffer, paramResult);
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }
}
