package org.benzeneframework.requst;

import org.benzeneframework.annotation.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeongukjae on 15. 10. 30..
 */
public class Request {
    private Route.RouteMethod method;
    private String path;
    private String version;
    private Map<String, String> headerOptions;
    private Map<String, String> requestParameters;
    private Map<String, String> queryParameters;
    public Request() {
        headerOptions = new HashMap<String, String>();
        requestParameters = new HashMap<String, String>();
        queryParameters = new HashMap<String, String>();
    }

    public void setMethod(Route.RouteMethod method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addHeader(String key, String value) {
        headerOptions.put(key, value);
    }

    public void setQuery(Map<String, String> parameters) {
        for(String key :parameters.keySet()) {
            queryParameters.put(key, parameters.get(key));
        }
    }

    public void setParameters(Map<String, String> parameters) {
        for(String key :parameters.keySet()) {
            requestParameters.put(key, parameters.get(key));
        }
    }

    public String getQuery(String key) {
        if(queryParameters.containsKey(key)) {
            return queryParameters.get(key);
        } else {
            return null;
        }
    }

    public String get(String key) {
        if(method == Route.RouteMethod.GET) {
            return getQuery(key);
        }

        if(requestParameters.containsKey(key)) {
            return requestParameters.get(key);
        } else {
            return null;
        }
    }

    public String getHeader(String key) {
        if(headerOptions.containsKey(key)) {
            return headerOptions.get(key);
        } else {
            return null;
        }
    }
}
