package me.lofro.game.greenLight;

import lombok.Getter;
import lombok.Setter;
import me.lofro.game.GameManager;
import me.lofro.game.greenLight.data.GreenLightData;
import me.lofro.game.greenLight.listeners.GreenLightListener;
import me.lofro.game.greenLight.state.LightState;
import me.lofro.game.greenLight.tasks.PlayerArrayQueueShootTask;
import me.lofro.global.utils.Commands;
import me.lofro.global.utils.Listeners;
import me.lofro.global.utils.Locations;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class GreenLightManager {

    private final GameManager gameManager;

    private GreenLightGame greenLightGame;

    private final GreenLightListener greenLightListener;

    private @Getter @Setter LightState lightState;

    private @Getter boolean isRunning = false;

    private final @Getter ArrayList<Player> eliminated = new ArrayList<>();

    private int shootAllTaskID;

    private @Setter World world;

    public GreenLightManager(final GameManager gameManager, World world) {
        this.gameManager = gameManager;

        this.greenLightListener = new GreenLightListener(this);

        Commands.registerCommands(gameManager.greenLightCore().paperCommandManager(), new GreenLightCMD(this));

        this.world = world;
    }

    public void start(int seconds) {
        start(seconds, 3, 10, 10, 20);
    }

    public void start(int seconds, int greenLowestTimeBound, int greenHighestTimeBound, int redLowestTimeBound, int redHighestTimeBound) {
        if (isRunning) throw new IllegalStateException("The GreenLightGame is already running.");

        isRunning = true;

        gameManager.gameTimer().start(seconds);

        Listeners.registerListeners(greenLightListener);

        this.greenLightGame = new GreenLightGame(this, greenLowestTimeBound, greenHighestTimeBound, redLowestTimeBound, redHighestTimeBound);

        greenLightGame.setTaskID(greenLightGame.runTaskTimer(gameManager().greenLightCore(), 0, 20).getTaskId());

        greenLightGame.toggleLight(LightState.GREEN);

        greenLightGame.setEndTaskID(Bukkit.getScheduler().runTaskLater(gameManager.greenLightCore(), this::endGame, seconds * 20L).getTaskId());
    }

    public void endGame() {
        greenLightGame.cancel();
        this.isRunning = false;
        this.eliminated.clear();

        greenLightGame.removeEffects();

        if (this.lightState.equals(LightState.GREEN)) Listeners.registerListener(greenLightListener);

        shootAllTaskID = Bukkit.getScheduler().runTaskLater(gameManager().greenLightCore(), () -> shootAll(true), 20 * 10).getTaskId();

        this.greenLightGame = new GreenLightGame(this);
    }

    public void stopGame() {
        Bukkit.getScheduler().cancelTask(greenLightGame.getEndTaskID());
        Bukkit.getScheduler().cancelTask(shootAllTaskID);

        greenLightGame.removeEffects();

        gameManager.gameTimer().end();

        greenLightGame.cancel();
        isRunning = false;
        eliminated.clear();

        Listeners.unregisterListeners(greenLightListener);

        this.greenLightGame = new GreenLightGame(this);
    }

    public void shoot(Player player) {
        if (player.getGameMode().equals(GameMode.SPECTATOR) || player.getGameMode().equals(GameMode.CREATIVE)) return;
        player.setHealth(0);
        eliminated.add(player);
    }

    public void shootAll(boolean endGame) {
        ArrayList<Player> playerList = new ArrayList<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (Locations.isInCube(areaLower(), areaUpper(), p.getLocation())) {
                playerList.add(p);
            }
        }
        new PlayerArrayQueueShootTask(this, playerList, endGame, 0, 40);
    }

    public GameManager gameManager() {
        return gameManager;
    }

    public GreenLightData greenLightData() {
        return gameManager.gameData().greenLightData();
    }

    /**
     * @return the world where the game is running.
     */
    public World world() {
        return world;
    }

    public GreenLightListener greenLightListener() {
        return greenLightListener;
    }

    public Location areaLower() {
        return greenLightData().areaLower();
    }

    public Location areaUpper() {
        return greenLightData().areaUpper();
    }

    public boolean isInCube(Location location) {
        return Locations.isInCube(areaLower(), areaUpper(), location);
    }

}
