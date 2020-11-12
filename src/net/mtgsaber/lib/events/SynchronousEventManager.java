package net.mtgsaber.lib.events;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Andrew Arnold (6/18/2019)
 */
public class SynchronousEventManager extends EventManager {
    public SynchronousEventManager() {
        super();
    }

    @Override
    public void addHandler(String name, Consumer<Event> handler) {
        if (HANDLER_MAP.containsKey(name)) {
            if (!HANDLER_MAP.get(name).contains(handler))
                HANDLER_MAP.get(name).add(handler);
        } else {
            LinkedList<Consumer<Event>> list = new LinkedList<>();
            list.add(handler);
            HANDLER_MAP.put(name, list);
        }
    }

    @Override
    public void removeHandler(String name, Consumer<Event> handler) {
        if (HANDLER_MAP.containsKey(name)) {
            List<Consumer<Event>> list = HANDLER_MAP.get(name);
            list.remove(handler);
        }
    }

    @Override
    public void push(Event e) {
        if (HANDLER_MAP.containsKey(e.getName()))
            for (Consumer<Event> handler : HANDLER_MAP.get(e.getName()))
                handler.accept(e);
    }
}
