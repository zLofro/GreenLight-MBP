package me.lofro.game.greenLight;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.lofro.game.GameManager;
import me.lofro.game.greenLight.state.LightState;
import me.lofro.game.greenLight.tasks.PlayerArrayQueueShootTask;
import me.lofro.global.utils.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

@Data
@EqualsAndHashCode(callSuper = false)
public class GreenLightGame extends BukkitRunnable {

    private final GameManager gameManager;
    private final GreenLightManager greenLightManager;

    private @Getter @Setter int taskID;
    private @Getter @Setter int endTaskID;

    private @Setter int greenLowestTimeBound;
    private @Setter int greenHighestTimeBound;

    private @Setter int redLowestTimeBound;
    private @Setter int redHighestTimeBound;

    private int timeBetween;

    public GreenLightGame(final GreenLightManager greenLightManager, int greenLowestTimeBound, int greenHighestTimeBound, int redLowestTimeBound, int redHighestTimeBound) {
        this.greenLightManager = greenLightManager;
        this.gameManager = greenLightManager.gameManager();

        this.greenLowestTimeBound = greenLowestTimeBound;
        this.greenHighestTimeBound = greenHighestTimeBound;

        this.redLowestTimeBound = redLowestTimeBound;
        this.redHighestTimeBound = redHighestTimeBound;

        this.timeBetween = ThreadLocalRandom.current().nextInt(greenLowestTimeBound, greenHighestTimeBound);
    }

    public GreenLightGame(final GreenLightManager greenLightManager) {
        this(greenLightManager, 3, 10, 10, 20);
    }

    @Override
    public void run() {
        if (timeBetween == 0) toggleLight(greenLightManager.getLightState() == LightState.GREEN ? LightState.RED : LightState.GREEN);

        if (greenLightManager.greenLightListener().getMovedList().size() > 0) {
            new PlayerArrayQueueShootTask(greenLightManager, greenLightManager.greenLightListener().getMovedList(), false, 0, 20);
            greenLightManager.greenLightListener().getMovedList().clear();
        }

        timeBetween--;
    }

    public void toggleLight(LightState lightState) {
        addEffects(lightState);

        if (lightState.equals(LightState.GREEN)) {
            this.timeBetween = ThreadLocalRandom.current().nextInt(greenLowestTimeBound, greenHighestTimeBound);
            greenLightManager.setLightState(LightState.GREEN);

            Bukkit.getOnlinePlayers().stream().filter(p -> greenLightManager.isInCube(p.getLocation())).forEach(p -> {
                if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            });

            Listeners.unregisterListener(greenLightManager.greenLightListener());
        } else {
            Bukkit.getScheduler().runTaskLater(gameManager.greenLightCore(), () -> {
                this.timeBetween = ThreadLocalRandom.current().nextInt(redLowestTimeBound, redHighestTimeBound);
                greenLightManager.setLightState(LightState.RED);
                Listeners.registerListener(greenLightManager.greenLightListener());
            }, 20);
        }
    }

    public void addEffects(LightState lightState) {
        if (lightState.equals(LightState.GREEN)) {
            Bukkit.getOnlinePlayers().stream().filter(p -> greenLightManager.isInCube(p.getLocation())).forEach(p -> {
                if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) p.removePotionEffect(PotionEffectType.BLINDNESS);
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            });
        } else {
            Bukkit.getOnlinePlayers().stream().filter(p -> greenLightManager.isInCube(p.getLocation())).forEach(p -> {
                if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                if (p.getGameMode().equals(GameMode.CREATIVE) || p.getGameMode().equals(GameMode.SPECTATOR)) return;
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
            });
        }
    }

    public void removeEffects() {
        Bukkit.getOnlinePlayers().stream().filter(p -> greenLightManager.isInCube(p.getLocation())).forEach(p -> {
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        });
    }

}
