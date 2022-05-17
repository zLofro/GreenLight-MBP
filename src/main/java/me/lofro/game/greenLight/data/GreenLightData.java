package me.lofro.game.greenLight.data;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GreenLightData {

    private @Setter Location areaLower, areaUpper;

    public GreenLightData(Location areaLower, Location areaUpper) {
        this.areaLower = areaLower;
        this.areaUpper = areaUpper;
    }

    public GreenLightData() {
        this(new Location(Bukkit.getWorlds().get(0), 0, 0, 0), new Location(Bukkit.getWorlds().get(0), 0, 0, 0));
    }

    public Location areaLower() {
        return areaLower;
    }

    public Location areaUpper() {
        return areaUpper;
    }

}
