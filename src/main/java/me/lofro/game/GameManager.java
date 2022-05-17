package me.lofro.game;

import com.google.common.collect.ImmutableList;
import me.lofro.GreenLightCore;
import me.lofro.data.types.GameData;
import me.lofro.data.utils.JsonConfig;
import me.lofro.game.greenLight.GreenLightManager;
import me.lofro.game.greenLight.data.GreenLightData;
import me.lofro.global.interfaces.Restorable;
import me.lofro.global.utils.timer.GameTimer;
import org.bukkit.Bukkit;

public class GameManager implements Restorable {

    private final GreenLightCore greenLightCore;
    private final GreenLightManager greenLightManager;

    private GameData gameData;

    private final GameTimer gameTimer;

    public GameManager(final GreenLightCore greenLightCore) {
        this.greenLightCore = greenLightCore;

        this.gameTimer = new GameTimer();
        gameTimer.runTaskTimer(greenLightCore, 0, 20L);

        this.greenLightManager = new GreenLightManager(this, Bukkit.getWorlds().get(0));

        restore(greenLightCore.dataManager().gameDataConfig());

        // Sets the location command completion.
        greenLightCore.paperCommandManager().getCommandCompletions().registerCompletion(
                "@location", c -> ImmutableList.of("x,y,z"));
    }

    @Override
    public void restore(JsonConfig jsonConfig) {
        if (jsonConfig.getJsonObject().entrySet().isEmpty()) {
            this.gameData = new GameData(new GreenLightData());
        } else {
            this.gameData = GreenLightCore.gson().fromJson(jsonConfig.getJsonObject(), GameData.class);
        }
    }

    @Override
    public void save(JsonConfig jsonConfig) {
        jsonConfig.setJsonObject(GreenLightCore.gson().toJsonTree(gameData).getAsJsonObject());
        try {
            jsonConfig.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GreenLightCore greenLightCore() {
        return greenLightCore;
    }

    /**
     * @return the game data.
     */
    public GameData gameData() {
        return gameData;
    }

    /**
     * @return the game Timer.
     */
    public GameTimer gameTimer() {
        return gameTimer;
    }

}
