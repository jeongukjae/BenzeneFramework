package test;

import org.benzeneframework.Benzene;
import org.benzeneframework.Jade4Benzene;
import test.controllers.MainRouter;

/**
 * Created by jeongukjae on 15. 10. 30..
 * @author jeongukjae
 *
 * view engine, public path, view path
 */
public class MainHandler {
    public static void main(String []args) {
        Benzene benzene = new Benzene();
        benzene.addController(MainRouter.class);
        benzene.set("view", "views");
        benzene.set("public", "public");
        benzene.set("view engine", Jade4Benzene.class);
        benzene.startServer(8080);
    }
}
