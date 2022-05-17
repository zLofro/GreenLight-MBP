package me.lofro;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.lofro.data.DataManager;
import me.lofro.data.adapters.LocationSerializer;
import me.lofro.game.GameManager;
import me.lofro.global.utils.ChatColorFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * GreenLight plugin's main class.
 *
 * @author <a href="https://github.com/zLofro">Lofro</a> - Developer.
 */
public class GreenLightCore extends JavaPlugin {

    private static GreenLightCore pluginInstance;

    private static final Gson gson = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location[].class, LocationSerializer.getArraySerializer()).setPrettyPrinting()
            .serializeNulls()
            .create();

    private GameManager gameManager;
    private DataManager dataManager;

    private PaperCommandManager paperCommandManager;

    @Override
    public void onEnable() {
        pluginInstance = this;

        paperCommandManager = new PaperCommandManager(this);

        try {
            this.dataManager = new DataManager(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.gameManager = new GameManager(this);

        Bukkit.getConsoleSender().sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&aEl plugin ha sido activado correctamente."));
    }

    @Override
    public void onDisable() {
        dataManager.save();

        Bukkit.getConsoleSender().sendMessage(ChatColorFormatter.stringToComponentWithPrefix("&aEl plugin ha sido desactivado correctamente."));
    }

    /**
     * @return the plugin manager instance.
     */
    public static GreenLightCore instance() {
        return pluginInstance;
    }

    /**
     * @return the plugin Gson object.
     */
    public static Gson gson() {
        return gson;
    }

    /**
     * @return the paper command manager.
     */
    public PaperCommandManager paperCommandManager() {
        return paperCommandManager;
    }

    /**
     * @return the game manager.
     */
    public GameManager gameManager() {
        return gameManager;
    }

    /**
     * @return the data manager.
     */
    public DataManager dataManager() {
        return dataManager;
    }

}
