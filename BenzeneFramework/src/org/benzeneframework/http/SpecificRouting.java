package org.benzeneframework.http;

import org.benzeneframework.Benzene;
import org.benzeneframework.annotation.Route;
import org.benzeneframework.requst.Request;
import org.benzeneframework.response.Response;

/**
 * Created by jeongukjae on 15. 11. 4..
 * @author jeongukjae
 *
 * This class provides specific routing such as 404 Error, 400 Error..
 * Routings will be appended in future.
 */
public class SpecificRouting {
    /**
     * For 404 Not Found Error
     * @param request request element
     * @param response response element
     */
    @Route(route="/404")
    public static void notFound(Request request, Response response) {
        response.setHeader("Content-Type", "text/html");
        response.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>404 Not Found</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <center>\n" +
                "        <h1>\n" +
                "            <strong>\n" +
                "                404 Not Found\n" +
                "            </strong>\n" +
                "        </h1>\n" +
                "        <hr>\n" +
                "        <p>\n" +
                "            Benzene" + Benzene.VersionName + "\n" +
                "        </p>\n" +
                "    </center>\n" +
                "</body>\n" +
                "</html>");
        response.end();
    }


    /**
     * For 500 Internal Server Error
     * @param request request element
     * @param response response element
     */
    @Route(route="/500")
    public static void internalServerError(Request request, Response response) {
        response.setHeader("Content-Type", "text/html");
        response.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>500 Internal Server Error</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <center>\n" +
                "        <h1>\n" +
                "            <strong>\n" +
                "                500 Internal Server Error\n" +
                "            </strong>\n" +
                "        </h1>\n" +
                "        <hr>\n" +
                "        <p>\n" +
                "            The Server encountered an internal error or misconfiguration and was unable to complete your request.\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            Please contact the server administrator, and inform them of the time the error occurred, and anything you might have done that may have caused the error.\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            More information about this error may be available in the server error log\n" +
                "        </p>\n" +
                "        <hr>\n" +
                "        <p>\n" +
                "            Benzene" + Benzene.VersionName + "\n" +
                "        </p>\n" +
                "    </center>\n" +
                "</body>\n" +
                "</html>");
        response.end();
    }
}
