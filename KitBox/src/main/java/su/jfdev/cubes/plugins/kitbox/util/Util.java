package su.jfdev.cubes.plugins.kitbox.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Jamefrus on 13.05.2015.
 */

public class Util {

    public static BufferedReader getResource(String str){
        try (InputStream inputStream = Util.class.getResourceAsStream("/" + str);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
            return bufferedReader;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String buildList(List<String> list){
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String str : list){
            if(!first) sb.append("\n");
            first = false;
            sb.append(str);
        }
        return sb.toString();
    }

//    public static final String convertUTF8to1251(String str){
//        return str.getBytes(Charset.forName("1251").newEncoder().){
//
//        }
//    }
}
