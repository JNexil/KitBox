package su.jfdev.cubes.plugins.kitbox.util;

/**
 * Created by Jamefrus on 24.05.2015.
 */
public class BadResponseException extends Exception {

    int code;

    public BadResponseException(int code) {
        super();
        this.code = code;
    }
}
