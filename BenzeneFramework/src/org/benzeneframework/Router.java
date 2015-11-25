package org.benzeneframework;

import org.benzeneframework.annotation.Route;

import java.lang.reflect.Method;

/**
 * Created by jeongukjae on 15. 11. 3..
 * @author jeongukjae
 *
 * Router Class
 * just for data
 */
public class Router {
    public Method method;
    public String path;
    public Route.RouteMethod routeMethod;
    public Router() {}
    public Router(Method method, String path, Route.RouteMethod routeMethod) {
        this.method = method;
        this.path = path;
        this.routeMethod = routeMethod;
    }
}
