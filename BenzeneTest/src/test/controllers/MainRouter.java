package test.controllers;

import org.benzeneframework.annotation.Route;
import org.benzeneframework.request.Request;
import org.benzeneframework.response.Response;

/**
 * Created by jeongukjae on 15. 10. 30..
 * @author jeongukjae
 *
 * Test Code
 */
public class MainRouter {
    @Route(route="/")
    public static void main(Request request, Response response) {
        response.cookie("cookie", "abcd");
        response.expireCookie("cookie2");
        response.render("test.jade");
    }
}
