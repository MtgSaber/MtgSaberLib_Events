package net.mtgsaber.lib.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventManager {
    protected final Map<String, List<EventHandler>> LISTENER_MAP;

    public EventManager() {
        this.LISTENER_MAP = new HashMap<>();
    }

    /**
     * Adds an <code>Event</code> to the <code>List</code>
     * of <code>Handler</code>s for the given event name.
     * @param name The name of the event to be listened for. This is a key in @field LISTENER_MAP.
     * @param handler The <code>EventHandler</code> to add, listening for this event name.
     */
    public abstract void addHandler(String name, EventHandler handler);

    public abstract void removeHandler(String name, EventHandler handler);

    public abstract void push(Event e);
}
