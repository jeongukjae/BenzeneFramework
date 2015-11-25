package org.benzeneframework.annotation;

import java.lang.annotation.*;


/**
 * Created by jeongukjae on 15. 10. 30..
 * @author jeongukjae
 *
 * Routing Annotation
 * Example  : @Route('/example'[, 'post'])
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface Route {
    enum RouteMethod {ALL, GET, POST, ETC}          // 메소드 정의 METHOD

    String route();                                 // 라우팅 할 곳 ROUTED PATH
    RouteMethod method() default RouteMethod.ALL;   // 메소드 METHOD
}
