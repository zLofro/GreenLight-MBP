package me.lofro.data.types;

import lombok.Setter;
import me.lofro.game.greenLight.data.GreenLightData;

public class GameData {

    private @Setter GreenLightData greenLightData;

    public GameData(GreenLightData greenLightData) {
        this.greenLightData = greenLightData;
    }

    public GameData() {
        this(new GreenLightData());
    }

    /**
     * @return the green light data.
     */
    public GreenLightData greenLightData() {
        return greenLightData;
    }

}
