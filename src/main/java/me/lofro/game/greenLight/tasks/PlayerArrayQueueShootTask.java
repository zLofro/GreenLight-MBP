package me.lofro.game.greenLight.tasks;

import me.lofro.game.greenLight.GreenLightManager;
import me.lofro.global.utils.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class PlayerArrayQueueShootTask implements Runnable {

    private final Queue<Player> playerQueue = new ArrayDeque<>();
    private final BukkitTask task;
    private final GreenLightManager gLightManager;
    private final boolean endGame;

    public PlayerArrayQueueShootTask(GreenLightManager gLightManager, List<Player> players, boolean endGame, int delay, int period) {
        playerQueue.addAll(players);
        this.gLightManager = gLightManager;
        this.endGame = endGame;
        this.task = Bukkit.getScheduler().runTaskTimer(gLightManager.gameManager().greenLightCore(), this, delay, period);
    }

    @Override
    public void run() {
        if (playerQueue.isEmpty()) {
            if (endGame) Listeners.unregisterListeners(gLightManager.greenLightListener());
            this.task.cancel();
        } else {
            gLightManager.shoot(playerQueue.poll());
        }
    }
}
