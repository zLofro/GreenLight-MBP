package me.lofro.data;

import me.lofro.GreenLightCore;
import me.lofro.data.utils.JsonConfig;

public class DataManager {

    private final GreenLightCore greenLightCore;

    private final JsonConfig gameDataConfig;

    public DataManager(final GreenLightCore greenLightCore) throws Exception {
        this.greenLightCore = greenLightCore;

        this.gameDataConfig = JsonConfig.config("gameData.json", greenLightCore);
    }

    public void save() {
        greenLightCore.gameManager().save(gameDataConfig);
    }

    /**
     * @return the game data config.
     */
    public JsonConfig gameDataConfig() {
        return gameDataConfig;
    }

}
