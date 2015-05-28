package su.jfdev.cubes.plugins.kitbox;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

/**
 * Created by Jamefrus on 08.05.2015.
 */
public class Main extends JavaPlugin {

    public static final BoxInfo BOX_INFO = new BoxInfo();
    private YamlConfiguration boxYaml;
    public final static boolean PEX_ENABLED = Bukkit.getPluginManager().getPlugin("PermissionsEx") != null;

    private static Main instance;

    @Override
    public void onEnable() {
        Main.instance = this;


        getServer().getPluginManager().registerEvents(new KitBoxListener(this), this);
        this.getCommand("kitbox").setExecutor(new KitBoxCMDs(this));
    }

    @Override
    public void onDisable() {
        saveBoxYaml();
    }

    public void reload(){
        this.reloadConfig();
        boxYaml = loadBoxYaml();
    }

    private void unZipConfigYml() {
        if(!getDataFolder().exists()) getDataFolder().mkdirs();
        File f = new File(this.getDataFolder(),"config.yml");
        if(f.exists()) return;
        try(InputStream resourceAsStream = Main.class.getResourceAsStream("config.yml");FileOutputStream fileOutputStream = new FileOutputStream(f)) {
            byte[] buffer = new byte[Short.MAX_VALUE];
            int n;
            while((n= resourceAsStream.read()) > 0){
                fileOutputStream.write(buffer,0,n);
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private YamlConfiguration loadBoxYaml() {
        YamlConfiguration yaml = null;
        try {
            File yamlFile = new File(getDataFolder(), Defaults.YAML_NAME);
            if (!yamlFile.exists()) {
                yamlFile.createNewFile();
            }
            yaml = YamlConfiguration.loadConfiguration(yamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return yaml;
    }

    public YamlConfiguration getBoxYaml() {
        if(boxYaml == null) boxYaml = loadBoxYaml();
        return boxYaml;
    }

    public boolean saveBoxYaml() {
        try {
            if(boxYaml != null)
            boxYaml.save(new File(getDataFolder(), Defaults.YAML_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static synchronized Main getInstance() {
        return instance;
    }

    public String getHelpText(){
        StringBuilder sb = new StringBuilder("");
        try(Reader reader = this.getTextResource("help.txt"); BufferedReader br = new BufferedReader(reader)){
            while(br.ready()){
                sb.append(br.readLine());
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }



}
