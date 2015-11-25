package org.benzeneframework;

import org.benzeneframework.annotation.Route;
import org.benzeneframework.http.ListeningThread;
import org.benzeneframework.http.SpecificRouting;
import org.benzeneframework.middleware.Preferences;
import org.benzeneframework.utils.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeongukjae, parkjuchan on 15. 11. 2..
 * @author jeongukaje
 */
public class Benzene {
    @SuppressWarnings("unused")
    public static final String VersionName = "0.0.2";
    @SuppressWarnings("unused")
    public static final int VersionCode = 2;

    private static final String TAG = "BenzeneFramework";
    private int serverPort = 80;
    private ServerSocket serverSocket;
    private ListeningThread listeningThread;
    private List<Router> router = new ArrayList<>();
    private List<Router> defaultRouter = new ArrayList<>();
    private List<String> publics = new ArrayList<>();
    private Preferences preferences;

    /**
     * Constructor
     * init fields.
     */
    public Benzene() {
        listeningThread = new ListeningThread(this);
        addController(SpecificRouting.class, defaultRouter);
        preferences = Preferences.getInstance();
    }

    /**
     * Start server
     * @return if server starts successfully, return true.
     */
    @SuppressWarnings("unused")
    public boolean startServer() {
        try {serverSocket = new ServerSocket(serverPort);
            Log.i(TAG, "Create Server Socket at port " + serverPort);

            listeningThread.setServerSocket(serverSocket);
            listeningThread.start();

            return true;
        } catch (IOException e) {
            Log.i(TAG, "Creating server failed");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Set server port
     * @param port port
     */
    @SuppressWarnings("unused")
    public void setPort(int port) {
        Log.i(TAG, "Set Port : " + port);
        this.serverPort = port;
    }

    /**
     * Stop server
     */
    @SuppressWarnings("unused")
    public void stopServer() {
        try {
            Log.i(TAG, "Stop Server");
            if (serverSocket.isBound()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add router
     *
     * @param controller controller class
     */
    @SuppressWarnings("unused")
    public void addController(Class controller) {
        addController(controller, router);
    }

    /**
     * Add Router to list
     *
     * @param controller controller class
     * @param routers router list
     */
    @SuppressWarnings("unused")
    private void addController(Class controller, List<Router> routers) {
        Method[] methods = controller.getDeclaredMethods();
        for(Method method : methods) {
            Route route = method.getAnnotation(Route.class);
            if(route != null) {
                routers.add(new Router(method, route.route(), route.method()));
            }
        }
    }

    /**
     * return not found method
     * @return not found method
     */
    @SuppressWarnings("unused")
    public Method notFound() {
        return getRouter(Route.RouteMethod.ALL, "/404");
    }

    /**
     * Return 500 page
     * @return 500 page method
     */
    @SuppressWarnings("unused")
    public Method serverError() {
        return getRouter(Route.RouteMethod.ALL, "/500");
    }

    /**
     * Get router method
     *
     * @param routeMethod http method
     * @param path router method's path
     * @return router method
     */
    @SuppressWarnings("unused")
    public Method getRouter(Route.RouteMethod routeMethod, String path) {
        Method m = getMethod(router, routeMethod, path);
        if(m == null) {
            return getMethod(defaultRouter, routeMethod, path);
        } else {
            return m;
        }
    }

    /**
     * Get Method
     *
     * @param routers router list
     * @param routeMethod router method
     * @param path router path
     * @return method
     */
    @SuppressWarnings("unused")
    private Method getMethod(List<Router> routers, Route.RouteMethod routeMethod, String path) {
        for (Router r : routers) {
            if ((r.routeMethod == routeMethod || r.routeMethod == Route.RouteMethod.ALL) && r.path.equals(path)) {
                return r.method;
            }
        }
        return null;
    }

    /**
     * add public path
     * @param path root path of public files
     */
    @SuppressWarnings("unused")
    public void addPublic(String path) {
        File f = new File(path);
        if(!f.isDirectory()) {
            Log.e(TAG, path + " is not directory!!");
        } else {
            publics.add(path);
        }
    }

    /**
     * get public file
     * @param path file's path
     * @return if file doesn't exist, return null
     */
    @SuppressWarnings("unused")
    public File getPublic(String path) {
        for(String pub:publics) {
            File f = new File(pub + path);
            if(f.exists() && !f.isDirectory()) {
                return f;
            }
        }
        return null;
    }

    /**
     * Set Middleware
     * @param key key
     * @param object value
     */
    @SuppressWarnings("unused")
    public void set(String key, Object object) {
        preferences.put(key, object);
    }
}
