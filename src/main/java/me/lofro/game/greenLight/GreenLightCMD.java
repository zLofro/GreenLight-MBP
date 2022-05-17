package me.lofro.game.greenLight;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import me.lofro.global.utils.ChatColorFormatter;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("greenLight")
@CommandPermission("admin.perm")
public class GreenLightCMD extends BaseCommand {

    private final GreenLightManager greenLightManager;

    public GreenLightCMD(final GreenLightManager greenLightManager) {
        this.greenLightManager = greenLightManager;
    }

    @Subcommand("start")
    @CommandCompletion("seconds greenLowestTimeBound greenHighestTimeBound redLowestTimeBound redHighestTimeBound")
    public void start(CommandSender sender, int seconds, int greenLowestTimeBound, int greenHighestTimeBound,
                      int redLowestTimeBound, int redHighestTimeBound) {
        if (greenLightManager.isRunning()) {
            sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&cEl juego ya está siendo ejecutado."));
        } else {
            greenLightManager.start(seconds, greenLowestTimeBound, greenHighestTimeBound, redLowestTimeBound, redHighestTimeBound);
            sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&7El juego ha sido iniciado correctamente."));
        }
    }

    @Subcommand("stop")
    public void stop(CommandSender sender) {
        if (greenLightManager.isRunning()) {
            greenLightManager.stopGame();
            sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&7El juego ha sido desactivado correctamente."));
        } else {
            sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&cEl juego no está siendo ejecutado."));
        }
    }

    @Subcommand("shoot")
    public void shoot(CommandSender sender, Player player) {
        greenLightManager.shoot(player);
        sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&7El jugador &8" + player.getName() + " &7ha sido disparado."));
    }

    @Subcommand("setArea")
    @CommandCompletion("@location @location")
    public void setArea(CommandSender sender, Location areaLower, Location areaUpper) {
        if (greenLightManager.isRunning()) {
            sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&cNo puedes actualizar el área mientras el juego está siendo ejecutado."));
        } else {
            var greenLightData = greenLightManager.greenLightData();

            greenLightData.setAreaLower(areaLower);
            greenLightData.setAreaUpper(areaUpper);

            sender.sendMessage(ChatColorFormatter.stringToStringWithPrefix("&7El área de juego ha sido actualizada con éxito."));
        }
    }

}
