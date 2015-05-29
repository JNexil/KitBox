package su.jfdev.cubes.plugins.kitbox.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jamefrus on 24.05.2015.
 */

public class UtilYaDisk {

    public static JSONObject getJSONResponce(URL url) throws IOException, ParseException, BadResponseException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() != 200) throw new BadResponseException(connection.getResponseCode());
        JSONObject jsonObject;
        try (InputStreamReader isr = new InputStreamReader(connection.getInputStream())) {
            Object parse = new JSONParser().parse(isr);
            jsonObject = (JSONObject) parse;
        }
        return jsonObject;
    }

    public static JSONObject getJSONResponse(String public_key, String path, boolean download) throws IOException, ParseException, BadResponseException {
        StringBuilder stringBuilder = new StringBuilder("https://cloud-api.yandex.net/v1/disk/public/resources");
        if (download) stringBuilder.append("/download");
        stringBuilder.append("?public_key=" + public_key);
        if (path != null && path.length() > 0) stringBuilder.append("&path=").append(path);
        URL url = new URL(stringBuilder.toString());
        return getJSONResponce(url);
    }

    public static String getDownloadURL(String public_key, String path) throws IOException, ParseException, BadResponseException {
        JSONObject jsonResponse = getJSONResponse(public_key, path, true);
        String href = (String) jsonResponse.get("href");
        return href;
    }
}
