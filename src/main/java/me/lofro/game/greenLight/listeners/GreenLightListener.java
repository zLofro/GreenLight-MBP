package me.lofro.game.greenLight.listeners;

import com.destroystokyo.paper.ParticleBuilder;
import lombok.Getter;
import me.lofro.game.greenLight.GreenLightManager;
import me.lofro.global.utils.Sounds;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class GreenLightListener implements Listener {

    private final GreenLightManager greenLightManager;

    private final @Getter ArrayList<Player> movedList = new ArrayList<>();

    public GreenLightListener(final GreenLightManager greenLightManager) {
        this.greenLightManager = greenLightManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        var location = player.getLocation();

        if (greenLightManager.isInCube(location)) {
            if (greenLightManager.isRunning()) {
                if (movedList.contains(player)) return;
                movedList.add(player);
            } else {
                if (player.getLocation().getBlockY() != player.getLocation().getY()) return;
                e.setCancelled(true);
            }
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            movedList.remove(player);
        }
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        var player = e.getPlayer();
        var location = player.getLocation();

        if (greenLightManager.isInCube(location)) {
            if (greenLightManager.isRunning()) {
                movedList.add(player);
            } else {
                if (player.getLocation().getBlockY() != player.getLocation().getY()) return;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getPlayer();
        var location = player.getLocation();

        if (greenLightManager.isInCube(location)) {
            Firework firework = (Firework) player.getWorld().spawnEntity(location, EntityType.FIREWORK);
            var meta = firework.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.STAR).withTrail().trail(true).withColor(Color.LIME, Color.WHITE).build());
            firework.setFireworkMeta(meta);

            Bukkit.getScheduler().runTaskLater(greenLightManager.gameManager().greenLightCore(), firework::detonate, 2);
        }
    }

}
