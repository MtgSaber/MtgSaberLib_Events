package net.mtgsaber.lib.events;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

/**
 * Author: Andrew Arnold 11/11/2020
 */
public final class AsynchronousEventManager extends EventManager implements Runnable {
    private final Queue<Event> EVENTS = new LinkedBlockingQueue<>();
    private volatile boolean running = true;
    private volatile int producerCount = 0;
    private final Semaphore queueMutex = new Semaphore(1);
    private final Object producerCountLock = new Object();
    private final Object suspensionLock = new Object();

    public AsynchronousEventManager() {
        queueMutex.release();
    }

    @Override
    public void run() {
        while (running) {
            try {
                /*
                if (!running) break;
                queueMutex.acquire(); // again, to make sure we don't miss any events
                if (!EVENTS.isEmpty()) // a producer made it to the queue while we were prepping for suspension
                    try {
                        throw new InterruptedException(); // process the event(s).
                    } finally {
                        queueMutex.release(); // allow ourselves to process or allow other producers to line up.
                    }
                 */
                synchronized (suspensionLock) {
                    suspensionLock.wait(10);
                    throw new InterruptedException();
                }
            } catch (InterruptedException ex) {
                if (!running) break;
                try { // this try catch block waits for all producers to queue their events.
                    queueMutex.acquire();
                } catch (InterruptedException ex2) {
                    if (!running) break;
                    continue;
                }
                while (!EVENTS.isEmpty()) { // process all events
                    Event e = EVENTS.remove();
                    List<Consumer<Event>> handlers;
                    synchronized (HANDLER_MAP) { // we need the map here
                        handlers = new LinkedList<>();
                        List<Consumer<Event>> mappedHandlers = HANDLER_MAP.get(e.getName());
                        if (mappedHandlers != null) // protects against nulls when there are no handlers for this event.
                            handlers.addAll(mappedHandlers);
                    }
                    for (Consumer<Event> handler : handlers)
                        handler.accept(e);
                }
                if (!running) break;
                queueMutex.release(); // for a very short moment
            }
        }
    }

    @Override
    public void addHandler(String name, Consumer<Event> handler) {
        synchronized (HANDLER_MAP) {
            if (HANDLER_MAP.containsKey(name))
                HANDLER_MAP.get(name).add(handler);
            else
                HANDLER_MAP.put(name, new LinkedList<>(Collections.singleton(handler)));
        }
    }

    @Override
    public void removeHandler(String name, Consumer<Event> handler) {
        synchronized (HANDLER_MAP) {
            final List<Consumer<Event>> handlers = HANDLER_MAP.get(name);
            if (handlers != null) {
                handlers.remove(handler);
                if (handlers.isEmpty())
                    HANDLER_MAP.remove(name);
            }
        }
    }

    /**
     * Uses group mutual exclusion with this manager's thread. Producers get group priority.
     * @param e The event to be "fired".
     */
    @Override
    public void push(Event e) {
        if (!running) return;

        synchronized (producerCountLock) {
            if (producerCount == 0) {
                try {
                    queueMutex.acquire();
                } catch (InterruptedException ex) {
                    return;
                }

                synchronized (suspensionLock) {
                    suspensionLock.notify();
                }
            }
            producerCount++;
        }
        EVENTS.add(e);
        synchronized (producerCountLock) {
            producerCount--;
            if (producerCount == 0) {
                queueMutex.release();
            }
        }
    }

    public void shutdown() {
        if (running) {
            running = false;
            synchronized (suspensionLock) {
                suspensionLock.notify();
            }
        }
    }
}
