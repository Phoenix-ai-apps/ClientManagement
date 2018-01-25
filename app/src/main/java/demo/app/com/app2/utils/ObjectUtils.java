package demo.app.com.app2.utils;

import java.util.List;

/**
 * Created by root on 26/12/17.
 */

public class ObjectUtils {

    public static boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }

}
