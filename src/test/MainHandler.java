package test;

import org.benzeneframework.Benzene;
import org.benzeneframework.Jade4Benzene;
import test.controllers.MainRouter;

/**
 * Created by jeongukjae on 15. 10. 30..
 */
public class MainHandler {
    public static void main(String []args) {
        Benzene benzene = new Benzene();
        benzene.setPort(8800);
        benzene.addController(MainRouter.class);
        benzene.addPublic("public");
        benzene.set("view", "views");
        benzene.set("view engine", Jade4Benzene.class);
        benzene.startServer();
    }
}
