package org.benzeneframework.response;

import org.benzeneframework.middleware.DefaultRenderer;
import org.benzeneframework.middleware.Preferences;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeongukjae on 15. 10. 30..
 * @author jeongukjae
 *
 * Response class will help you responsing http request.
 */
public class Response {
    private static final String TAG = "Response";

    private Socket socket;
    private DataOutputStream dos;
    private StringBuilder content = new StringBuilder();
    private int responseCode = 200;
    private String responseString = "OK";
    private Map<String, String> headerOptions = new HashMap<>();
    private OnErrorListener onErrorListener;

    public Response(Socket socket, DataOutputStream dos) {
        this.socket = socket;
        this.dos = dos;
        headerOptions.put("Content-Type", "text/html");
        headerOptions.put("Server", "Benzene/0.0.1");
        headerOptions.put("Connection", "close");
    }

    public void write(String msg) {
        content.append(msg);
    }

    public void send(String path) {
        try {
            BufferedReader f;
            try {
                f = new BufferedReader(new FileReader(path));
            } catch( FileNotFoundException e ) {
                f = new BufferedReader(new InputStreamReader(Response.class.getResourceAsStream(path)));
            }
            String content = "";
            String tmp;
            while((tmp = f.readLine()) != null) {
                content += tmp;
            }

            writeHeader();
            dos.writeBytes(content);
        } catch( IOException e ) {
            // On Error
            if(onErrorListener != null) {
                onErrorListener.onError("Output Stream Writing Error", socket);
            }
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void writeHeader() {
        try {
            dos.writeBytes("HTTP/1.1 " + responseCode + " " + responseString + "\r\n");
            for (String key : headerOptions.keySet()) {
                dos.writeBytes(key + ": " + headerOptions.get(key) + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            // On Error
            if(onErrorListener != null) {
                onErrorListener.onError("Output Stream Writing Error", socket);
            }
            e.printStackTrace();
        }
    }

    public void end() {
        try {
            writeHeader();
            dos.writeBytes(content.toString());
        } catch (IOException e) {
            e.printStackTrace();

            // On Error
            if(onErrorListener != null) {
                onErrorListener.onError("Output Stream Writing Error", socket);
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void render(String path) {
        render(path, null);
    }

    public void render(String path, Map<String, ?> params) {
        Preferences preference = Preferences.getInstance();
        Class c = (Class)preference.get("view engine");
        String result = null;
        if(c != null) {
            try {
                @SuppressWarnings("unchecked")
                Method render = c.getDeclaredMethod("render", String.class, Map.class);
                @SuppressWarnings("unchecked")
                Constructor constructor = c.getConstructor();
                result = (String)render.invoke(constructor.newInstance(), path, params);
            } catch (NoSuchMethodException|InvocationTargetException|
                    IllegalAccessException|InstantiationException e) {
                e.printStackTrace();
                onErrorListener.onError("Rendering Method Error", socket);
            }
        } else {
            DefaultRenderer defaultRenderer = new DefaultRenderer();
            result = defaultRenderer.render(path, params);
        }

        if(result == null) {
            onErrorListener.onError("Rendering Result Problem", socket);
            return;
        }

        try {
            writeHeader();
            dos.writeBytes(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCode(int val) {
        responseCode = val;
    }

    public void setResponseString(String val) {
        responseString = val;
    }

    public void setHeader(String key, String val) {
        headerOptions.put(key, val);
    }

    public void setOnErrorListener(OnErrorListener listener) {
        onErrorListener = listener;
    }

    public interface OnErrorListener {
        void onError(String msg, Socket socket);
    }
}
