package org.benzeneframework.library;

/**
 * Created by jeongukjae on 2015. 12. 17..
 * @author jeongukjae
 *
 * Define Library's executing method.
 */
@SuppressWarnings("unused")
public interface CommonLibrary extends BenzeneLibrary {
    /**
     * This method will be execute Library's main function.
     *
     * @param objects parameters that Library requires
     */
    @SuppressWarnings("unused")
    Object execute(Object ... objects);
}
