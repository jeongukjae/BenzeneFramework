package org.benzeneframework.requst;

import org.benzeneframework.annotation.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeongukjae on 15. 10. 30..
 *
 * Request Class.
 * Request Class will help you getting url query parameters, request body parameters, and get HTTP Options.
 */
public class Request {
    private Route.RouteMethod method;
    private String path;
    private String version;
    private Map<String, String> headerOptions;
    private Map<String, String> requestParameters;
    private Map<String, String> queryParameters;

    /**
     * Constructor
     */
    public Request() {
        headerOptions = new HashMap<>();
        requestParameters = new HashMap<>();
        queryParameters = new HashMap<>();
    }

    /**
     * Set Method.
     * Maybe you don't have to use this method.
     * @param method HTTP method
     */
    @SuppressWarnings("unused")
    public void setMethod(Route.RouteMethod method) {
        this.method = method;
    }

    /**
     * Set Path
     * You don't have to use this method.
     * @param path path
     */
    @SuppressWarnings("unused")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Set HTTP Version
     * You don't have to use this method.
     * @param version http version
     */
    @SuppressWarnings("unused")
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * add Header
     * You don't have to use this method.
     * @param key http header option key
     * @param value http header option value
     */
    @SuppressWarnings("unused")
    public void addHeader(String key, String value) {
        headerOptions.put(key, value);
    }

    /**
     * Set Query.
     * Query means Http method "GET"'s url parameters
     * You don't have to use this method.
     * @param parameters query parameters
     */
    @SuppressWarnings("unused")
    public void setQuery(Map<String, String> parameters) {
        for(String key :parameters.keySet()) {
            queryParameters.put(key, parameters.get(key));
        }
    }

    /**
     * Set Body Parameters
     * You don't have to use this method.
     *
     * @param parameters parameters
     */
    @SuppressWarnings("unused")
    public void setParameters(Map<String, String> parameters) {
        for(String key :parameters.keySet()) {
            requestParameters.put(key, parameters.get(key));
        }
    }

    /**
     * GET Query Parameters
     * You don't have to use this method.
     * if http method is get, you can use #{@link #get(String)} instead of this.
     *
     * @param key param's name
     * @return param's value
     */
    @SuppressWarnings("unused")
    public String getQuery(String key) {
        if(queryParameters.containsKey(key)) {
            return queryParameters.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get Params
     * If http method is get, it will return url's parameter.
     * If not, It will be return body parameter.
     *
     * @param key param's name
     * @return param's value
     */
    @SuppressWarnings("unused")
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

    /**
     * Get Header (HTTP Option)
     *
     * @param key header's key
     * @return header's value
     */
    @SuppressWarnings("unused")
    public String getHeader(String key) {
        if(headerOptions.containsKey(key)) {
            return headerOptions.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get Path
     *
     * @return request path
     */
    @SuppressWarnings("unused")
    public String getPath() {
        return path;
    }

    /**
     * Get Version
     *
     * @return http version
     */
    @SuppressWarnings("unused")
    public String getVersion() {
        return version;
    }
}
