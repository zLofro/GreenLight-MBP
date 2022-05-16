package me.lofro.global.interfaces;

/**
 * A class that allows you to get the instance of a given class.
 */
public abstract class Instantiable<T> {
    private final T instance;

    /**
     * Default constructor for bukkit instantiation.
     * 
     * @param instance The class instance.
     */
    public Instantiable(T instance) {
        this.instance = instance;
    }

    /**
     * A getter that returns the given class instance.
     * 
     * @return The given class instance.
     */
    protected T instance() {
        return instance;
    }

}
