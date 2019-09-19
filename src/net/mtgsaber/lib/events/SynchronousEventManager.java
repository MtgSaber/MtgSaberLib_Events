package net.mtgsaber.lib.events;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: Andrew Arnold (6/18/2019)
 */
public class SynchronousEventManager extends EventManager {
    public SynchronousEventManager() {
        super();
    }

    @Override
    public void addHandler(String name, EventHandler handler) {
        if (LISTENER_MAP.containsKey(name)) {
            if (!LISTENER_MAP.get(name).contains(handler))
                LISTENER_MAP.get(name).add(handler);
        } else {
            LinkedList<EventHandler> list = new LinkedList<>();
            list.add(handler);
            LISTENER_MAP.put(name, list);
        }
    }

    @Override
    public void removeHandler(String name, EventHandler handler) {
        if (LISTENER_MAP.containsKey(name)) {
            List<EventHandler> list = LISTENER_MAP.get(name);
            list.remove(handler);
        }
    }

    @Override
    public void push(Event e) {
        if (LISTENER_MAP.containsKey(e.getName()))
            for (EventHandler handler : LISTENER_MAP.get(e.getName()))
                handler.handle(e);
    }
}
