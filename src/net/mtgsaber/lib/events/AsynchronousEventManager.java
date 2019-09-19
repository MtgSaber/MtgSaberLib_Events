package net.mtgsaber.lib.events;

import net.mtgsaber.lib.threads.Tickable;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class AsynchronousEventManager extends EventManager implements Tickable {
    private final Vector<Event> EVENTS;

    public AsynchronousEventManager() {
        this.EVENTS = new Vector<>();
    }

    @Override
    public void addHandler(String name, EventHandler handler) {
        synchronized (this) {
            if (LISTENER_MAP.containsKey(name)) {
                if (!LISTENER_MAP.get(name).contains(handler))
                    LISTENER_MAP.get(name).add(handler);
            } else {
                LinkedList<EventHandler> list = new LinkedList<>();
                list.add(handler);
                LISTENER_MAP.put(name, list);
            }
        }
    }

    @Override
    public void removeHandler(String name, EventHandler handler) {
        synchronized (this) {
            if (LISTENER_MAP.containsKey(name)) {
                List<EventHandler> list = LISTENER_MAP.get(name);
                list.remove(handler);
            }
        }
    }

    @Override
    public void push(Event e) {
        EVENTS.add(e);
    }

    @Override
    public void tick() {
        synchronized (EVENTS) {
            for (Event e : EVENTS)
                if (super.LISTENER_MAP.containsKey(e.getName()))
                    for (EventHandler handler : super.LISTENER_MAP.get(e.getName()))
                        handler.handle(e);
            EVENTS.clear();
        }
    }
}
