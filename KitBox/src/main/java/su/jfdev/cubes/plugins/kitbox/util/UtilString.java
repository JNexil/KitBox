package su.jfdev.cubes.plugins.kitbox.util;

import java.util.List;

/**
 * Created by Jamefrus on 24.05.2015.
 */

public class UtilString {
    public static String buildList(List<String> list) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String str : list) {
            if (!first) sb.append("\n");
            first = false;
            sb.append(str);
        }
        return sb.toString();
    }

    public static String buildPath(String... strings) {
        StringBuilder stringBuilder = new StringBuilder("/");
        for (String string : strings) {
            stringBuilder.append(string);
            stringBuilder.append("/");
        }
        return stringBuilder.toString();
    }

    public static String buildString(String... strings) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
