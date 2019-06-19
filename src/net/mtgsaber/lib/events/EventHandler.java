package net.mtgsaber.lib.events;

import java.util.EventListener;

/**
 * Author: Andrew Arnold (6/17/2019)
 */
public interface EventHandler extends EventListener {
    void handle(Event e);
}
