package org.benzeneframework.response;

import org.benzeneframework.Benzene;
import org.benzeneframework.library.Renderer;
import org.benzeneframework.middleware.DefaultRenderer;
import org.benzeneframework.middleware.Preferences;

import java.io.*;
import java.lang.reflect.Constructor;
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
    @SuppressWarnings("unused")
    private static final String TAG = "Response";
    private Socket socket;
    private DataOutputStream dos;
    private StringBuilder content = new StringBuilder();
    private int responseCode = 200;
    private String responseString = "OK";
    private Map<String, String> headerOptions = new HashMap<>();
    private Map<String, String> cookieOptions;
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
            // write cookie
            if(cookieOptions != null) {
                for(String key : cookieOptions.keySet()) {
                    dos.writeBytes("Set-Cookie: " + key + "=" + cookieOptions.get(key) + "\r\n");
                }
            }
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
                @SuppressWarnings("unchecked") Constructor constructor = c.getConstructor();
                Renderer o = (Renderer)c.newInstance();
                // check library type
                if(o.getType().equals("view engine"))
                    result = o.render(path, params);
                else
                    onErrorListener.onError("Rendering engine Error", socket);
            } catch (NoSuchMethodException|
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
     * Set Cookie
     * if you want to set cookie, use this method.
     * I referred to RFC6265.
     * @see <a href='http://tools.ietf.org/html/rfc6265#section-4.1.1'>RFC6265</a>
     *
     * @param key Cookie's key
     * @param val Cookie's value
     */
    @SuppressWarnings("unused")
    public void cookie(String key, String val) {
        if(cookieOptions == null) {
            cookieOptions = new HashMap<>();
        }
        cookieOptions.put(key, val);
    }

    /**
     * Remove Cookie
     * use this method to remove cookie
     * I referred to RFC6265.
     * @see <a href='http://tools.ietf.org/html/rfc6265#section-4.1.1'>RFC6265</a>
     *
     * @param key cookie's key which you want to delete
     */
    @SuppressWarnings("unused")
    public void expireCookie(String key) {
        if(cookieOptions == null) {
            cookieOptions = new HashMap<>();
        }
        cookieOptions.put(key, "; Expires=Sun, 06 Nov 1994 08:49:37 GMT");
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
