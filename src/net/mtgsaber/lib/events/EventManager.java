package net.mtgsaber.lib.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class EventManager {
    protected final Map<String, List<Consumer<Event>>> HANDLER_MAP;

    public EventManager() {
        this.HANDLER_MAP = new HashMap<>();
    }

    /**
     * Adds an <code>Event</code> to the <code>List</code>
     * of <code>Handler</code>s for the given event name.
     * @param name The name of the event to be listened for. This is a key in @field LISTENER_MAP.
     * @param handler The <code>Consumer<Event></code> to add, listening for this event name.
     */
    public abstract void addHandler(String name, Consumer<Event> handler);

    /**
     * Removes an <code>Event</code> from the <code>List</code>
     * of <code>Handler</code>s for the given event name.
     * @param name The name of the event to be listened for. This is a key in @field LISTENER_MAP.
     * @param handler The <code>Consumer<Event></code> to remove, listening for this event name.
     */
    public abstract void removeHandler(String name, Consumer<Event> handler);

    /**
     * Pushes an <code>Event</code> onto this manager. The child class must at some point call
     * <code>handle(e)</code> from each of this <code>e</code>'s registered <code>Consumer</code>s.
     * @param e The event to be "fired".
     */
    public abstract void push(Event e);
}
