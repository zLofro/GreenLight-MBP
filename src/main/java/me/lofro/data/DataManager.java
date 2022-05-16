package me.lofro.data;

import me.lofro.GreenLightCore;
import me.lofro.data.utils.JsonConfig;
import me.lofro.global.interfaces.Restorable;

public class DataManager implements Restorable {

    private final GreenLightCore greenLightCore;

    public DataManager(final GreenLightCore greenLightCore) {
        this.greenLightCore = greenLightCore;
    }

    @Override
    public void restore(JsonConfig jsonConfig) {

    }

    @Override
    public void save(JsonConfig jsonConfig) {

    }

}
