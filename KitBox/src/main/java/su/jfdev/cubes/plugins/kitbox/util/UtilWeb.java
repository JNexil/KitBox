package su.jfdev.cubes.plugins.kitbox.util;

import org.bukkit.configuration.file.YamlConfiguration;
import su.jfdev.cubes.plugins.kitbox.Main;
import su.jfdev.cubes.plugins.kitbox.yaml.Config;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jamefrus on 13.05.2015.
 */

public class UtilWeb {

    public static File downloadFile(String url) {
        return downloadFile(url, Main.getInstance().getDataFolder(), true);
    }

    public static YamlConfiguration downloadYaml(String file) {
        if (Config.YA_DISK.getBoolean()) return downloadYamlFromYaDisk(file);
        else
            return YamlConfiguration.loadConfiguration(downloadFile(Config.WEB_SERVER.getString() + file, Main.getInstance().getDataFolder(), false));
    }

    public static YamlConfiguration downloadYamlFromYaDisk(String file) {
        try {
            return YamlConfiguration.loadConfiguration(downloadFile(UtilYaDisk.getDownloadURL(Config.YA_DISK_DIR.getString(), file), Main.getInstance().getDataFolder(), false));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File downloadFile(String url, File directory, boolean redownload) {
        try {
            return downloadFile(new URL(url.replace("https", "http")), directory, redownload);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static File downloadFile(URL url, File directory, boolean redownload) {
        File dir = directory;
        if (!dir.exists()) dir.mkdirs();
        String fileName = null;
        for (String str : url.getQuery().split("&")) {
            if (str.contains("filename=")) {
                fileName = str.replaceAll("filename=", "");
                break;
            }
        }
        File file = new File(dir, fileName);
        if (file.exists() && file.length() > 0 && !redownload) {
            return file;
        }
        try {
            file.createNewFile();
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() / 100 != 2) {
                    return null;
                }
                try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                    try (BufferedWriter fw = new BufferedWriter(new FileWriter(file))) {
                        while (br.ready()) {
                            String line = br.readLine();
                            fw.append(line).append("\n");
                        }
                        fw.flush();
                    }
                }
            }
        } catch (IOException e) {
            return null;
        }
        return file;
    }

//    public static final String convertUTF8to1251(String str){
//        return str.getBytes(Charset.forName("1251").newEncoder().){
//
//        }
//    }
}
