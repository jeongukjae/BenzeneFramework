package test.controllers;

import org.benzeneframework.annotation.Route;
import org.benzeneframework.requst.Request;
import org.benzeneframework.response.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeongukjae on 15. 10. 30..
 * @author jeongukjae
 *
 * Test Code
 */
public class MainRouter {
    // path : get, method : ALL
    @Route(route="/")
    public static void index(Request request, Response response) {
        Map<String, Object> params = new HashMap<>();
        String smile = request.get("smile");    // get Smile parameters
        params.put("smile", smile);             // put params to render
        response.render("test.jade", params);     // render
    }

    // path : post, method : post
    @Route(route="/post", method = Route.RouteMethod.POST)
    public static void aaPost(Request request, Response response) {
        response.write("name:" + request.get("name"));  // response just string
        response.end();                                 // end response
    }
}
