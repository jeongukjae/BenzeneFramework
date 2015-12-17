package org.benzeneframework.http;

import org.benzeneframework.Benzene;
import org.benzeneframework.annotation.Route;
import org.benzeneframework.library.CommonLibrary;
import org.benzeneframework.library.LibrarySettings;
import org.benzeneframework.request.Request;
import org.benzeneframework.response.Response;
import org.benzeneframework.utils.Log;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by jeongukjae on 15. 11. 3..
 * @author jeongukjae, parkjuchan
 *
 * AcceptThread.
 * This thread will be created to process http request in {@link ListeningThread}.
 */
public class AcceptThread extends Thread {
    private static final String TAG = "AcceptThread";
    private Benzene benzene;
    private Socket socket;
    private Response.OnErrorListener onErrorListener = new Response.OnErrorListener() {
        @Override
        public void onError(String msg, Socket socket) {
            Log.d(TAG, "Error is occurred!! Error message : " + msg);
            try {
                Response response = new Response(socket, new DataOutputStream(socket.getOutputStream()));
                Method method = benzene.serverError();
                method.invoke(null, null, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Constructor
     *
     * @param benzene Main Server Object
     * @param socket Server's socket
     */
    public AcceptThread(Benzene benzene, Socket socket) {
        this.benzene = benzene;
        this.socket = socket;
    }

    @Override
    public void run() {
        // open output, input stream
        try(DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader bis = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // parse request code
            String startHeader = bis.readLine();
            if(startHeader == null) {
                socket.close();
                return;
            }

            StringTokenizer stringTokenizer = new StringTokenizer(startHeader);

            String methodString = stringTokenizer.nextToken();
            Route.RouteMethod method = getMethod(methodString);
            String path = stringTokenizer.nextToken();

            // parse parameter, query
            Map<String, String> query = new HashMap<>();
            Map<String, String> parameters = new HashMap<>();
            String absolutePath = getRealPath(path, query);

            File tempFile;
            if((tempFile = benzene.getPublic(absolutePath)) != null) {
                sendPublic(tempFile, dos);
                return;
            }

            // make response, request object for current http request
            Request request = new Request();
            Response response = new Response(socket, dos);
            request.setPath(absolutePath);
            request.setMethod(method);
            request.setVersion(stringTokenizer.nextToken());
            request.setQuery(query);
            response.setOnErrorListener(onErrorListener);
            Method function = benzene.getRouter(method, absolutePath);

            // parse http request options
            String tmp;
            int contentLength = 0;
            while((tmp = bis.readLine()) != null) {
                if(tmp.trim().length() == 0)
                    break;
                String key = tmp.substring(0, tmp.indexOf(":")).trim();
                String value = tmp.substring(tmp.indexOf(":") + 1).trim();
                request.addHeader(key, value);

                if(key.equalsIgnoreCase("Content-Length")) {
                    contentLength = Integer.parseInt(value);
                }
                if(key.equalsIgnoreCase("Cookie")) {
                    String[] cookies = value.split(";");
                    for(String cookie: cookies) {
                        String[] cookieTmp = cookie.trim().split("=");
                        request.addCookie(cookieTmp[0], cookieTmp[1]);
                    }
                }
            }

            //parse body
            if(contentLength != 0) {
                String body;
                char[] bodyBuffer = new char[contentLength];
                bis.read(bodyBuffer, 0, contentLength);
                body = new String(bodyBuffer);

                if (!body.trim().equals("")) {
                    String[] bodies = body.split("&");
                    for (String param : bodies) {
                        String[] splitParam = param.split("=");
                        parameters.put(splitParam[0], splitParam.length > 1 ? splitParam[1] : "");
                    }
                    request.setParameters(parameters);
                }
            }

            /* Library extension */
            Map<String, Object> option = new HashMap<>();
            for(String type:LibrarySettings.typesBeforeInvoking) {
                Class c = (Class)benzene.get(type);
                CommonLibrary commonLibrary = (CommonLibrary) c.newInstance();
                if(type.equals(commonLibrary.getType())) {
                    switch(type) {
                        case "session":{
                            option.put(type, commonLibrary.execute(request));
                            break;
                        }
                        default: {
                            option.put(type, commonLibrary.execute());
                        }
                    }
                }
            }

            // invoke function
            if(function != null) {
                if(function.getParameterCount() == 2)
                    function.invoke(null, request, response);
                else if(function.getParameterCount() == 3)
                    function.invoke(null, request, response, option);
                else
                    throw new Exception();
            } else {
                // if not found
                function = benzene.notFound();
                function.invoke(null, request, response);
            }
        } catch (Exception e) {
            // invoke internal server error
            Method function = benzene.serverError();
            try {
                if(socket.isClosed()) {
                    Response response = new Response(socket, new DataOutputStream(socket.getOutputStream()));
                    try {
                        function.invoke(null, null, response);
                    } catch (IllegalAccessException | InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // close socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get method with method string
     *
     * @param method method String
     * @return Method
     */
    private Route.RouteMethod getMethod(String method) {
        switch(method) {
            case "GET":
                return Route.RouteMethod.GET;
            case "POST":
                return Route.RouteMethod.POST;
            default:
                return Route.RouteMethod.ETC;
        }
    }

    /**
     * Get Real Path and Url Query
     *
     * @param path path
     * @param parameters Parameter Map to put url query
     * @return Real Path
     */
    private String getRealPath(String path, Map<String, String> parameters) {
        String[] splitPath = path.split("\\?");
        String realPath = splitPath[0];

        if(splitPath.length > 1) {
            String query = splitPath[1];

            String[] params = query.split("&");
            for (String param : params) {
                String[] splitParam = param.split("=");
                parameters.put(splitParam[0], splitParam[1]);
            }
        }
        return realPath;
    }

    /**
     * Serving Public Files
     *
     * @param f File Object created with public file's path
     * @param dos write Data using this object
     */
    private void sendPublic(File f, DataOutputStream dos) {
        try {

            String result = "";
            try(BufferedReader bis = new BufferedReader(new FileReader(f))){
                String line;
                while((line = bis.readLine()) != null) {
                    result += line;
                }
            }
            byte[] bytes = result.getBytes("UTF-8");


            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Server: Benzene/0.0.1\r\n");
            dos.writeBytes("Connection: close\r\n");
            dos.writeBytes("Content-Type: " + getFileMimeType(f.getPath()) + "\r\n");
            dos.writeBytes("Content-Length: " + bytes.length + "\r\n");
            dos.writeBytes("\r\n");

            dos.writeBytes(new String(bytes));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private String getFileMimeType(String path) {
        String fileExtension= path.substring(path.lastIndexOf(".")).toLowerCase();
        switch (fileExtension) {
            case ".html":
                return "text/html";
            case ".css":
                return "text/css";
            case ".js":
                return "application/javascript";
            case ".gif":
                return "image/gif";
            case ".png":
                return "image/png";
            case ".txt":
                return "text/plain";
            case ".xml":
                return "application/xml";
            case ".json":
                return "application/json";
            default:
                return MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(path);
        }
    }
}
