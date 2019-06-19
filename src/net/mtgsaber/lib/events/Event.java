package net.mtgsaber.lib.events;

/**
 * Author: Andrew Arnold (6/17/2019)
 */
public abstract class Event {
    protected final String NAME;

    public Event(String NAME) {
        this.NAME = NAME;
    }

    public String getName() {
        return NAME;
    }
}
