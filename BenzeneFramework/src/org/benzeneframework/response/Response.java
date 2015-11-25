package org.benzeneframework.response;

import org.benzeneframework.Benzene;
import org.benzeneframework.middleware.DefaultRenderer;
import org.benzeneframework.middleware.Preferences;
import org.benzeneframework.utils.Log;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeongukjae, parkjuchan on 15. 10. 30..
 * @author jeongukjae, parkjuchan
 *
 * Response class will help you responsing http request.
 */
public class Response {
    private Socket socket;
    private DataOutputStream dos;
    private StringBuilder content = new StringBuilder();
    private int responseCode = 200;
    private String responseString = "OK";
    private Map<String, String> headerOptions = new HashMap<>();
    private OnErrorListener onErrorListener;

    /**
     * Constructor
     *
     * @param socket socket for response
     * @param dos outputstream for response
     */
    public Response(Socket socket, DataOutputStream dos) {
        this.socket = socket;
        this.dos = dos;
        headerOptions.put("Content-Type", "text/html");
        headerOptions.put("Server", "Benzene/" + Benzene.VersionName);
        headerOptions.put("Connection", "close");
    }

    /**
     * Write content(String)
     *
     * @param msg String to write
     */
    @SuppressWarnings("unused")
    public void write(String msg) {
        content.append(msg);
    }

    /**
     * send file
     *
     * @param path file's path
     */
    @SuppressWarnings("unused")
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
            byte[] bytes = content.getBytes("UTF-8");
            dos.write(bytes, 0, bytes.length);
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

    /**
     * Write Header
     *
     * Write HTTP Options.
     * If you want to change http's option, call methods that help you change http's option.
     * ({@link #setCode(int)}, {@link #setHeader(String, String)}, {@link #setResponseString(String)})
     */
    @SuppressWarnings("unused")
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

    /**
     * If you response http request using {@link #write(String)}, Use this method to end response.
     *
     */
    @SuppressWarnings("unused")
    public void end() {
        try {
            writeHeader();
            byte[] bytes = content.toString().getBytes("UTF-8");
            dos.write(bytes, 0, bytes.length);
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

    /**
     * Render file with view engine
     *
     * @param path file's path
     */
    @SuppressWarnings("unused")
    public void render(String path) {
        render(path, null);
    }

    /**
     * Render file with view engine
     *
     * @param path file's path
     * @param params parameters
     */
    @SuppressWarnings("unused")
    public void render(String path, Map<String, ?> params) {
        Preferences preference = Preferences.getInstance();
        Class c = (Class)preference.get("view engine");
        String result = null;
        if(c != null) {
            try {
                @SuppressWarnings("unchecked") Method render = c.getDeclaredMethod("render", String.class, Map.class);
                @SuppressWarnings("unchecked") Method getType = c.getDeclaredMethod("getType");
                @SuppressWarnings("unchecked") Constructor constructor = c.getConstructor();
                Object o = c.newInstance();
                String type = (String)getType.invoke(o);
                // check library type
                if(type.equals("view engine"))
                    result = (String)render.invoke(o, path, params);
                else
                    onErrorListener.onError("Rendering engine Error", socket);
            } catch (NoSuchMethodException|InvocationTargetException|
                    IllegalAccessException|InstantiationException e) {
                e.printStackTrace();
                onErrorListener.onError("Rendering Method Error", socket);
                return;
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
            byte[] bytes = result.getBytes("UTF-8");
            dos.write(bytes, 0, bytes.length);
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

    /**
     * Set Response code
     * @param val code
     */
    @SuppressWarnings("unused")
    public void setCode(int val) {
        responseCode = val;
    }

    /**
     * Response String.
     * "OK" of "HTTP/1.1 200 OK"
     *
     * @param val response string
     */
    @SuppressWarnings("unused")
    public void setResponseString(String val) {
        responseString = val;
    }

    /**
     * Set Custom Header
     *
     * @param key header's key
     * @param val header's value
     */
    @SuppressWarnings("unused")
    public void setHeader(String key, String val) {
        headerOptions.put(key, val);
    }

    /**
     * Set Error Listener
     *
     * @param listener Error Listener
     */
    @SuppressWarnings("unused")
    public void setOnErrorListener(OnErrorListener listener) {
        onErrorListener = listener;
    }

    /**
     * On Error Listener
     * If there is some error, this interface's {@link #onError(String, Socket)} will be called.
     */
    public interface OnErrorListener {
        /**
         * On Error
         *
         * @param msg error message
         * @param socket error socket
         */
        void onError(String msg, Socket socket);
    }
}
