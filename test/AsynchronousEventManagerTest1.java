import net.mtgsaber.lib.events.AsynchronousEventManager;

public class AsynchronousEventManagerTest1 {
    public static void main(String[] args) {
        for (int i = 0; i < 500; i++) {
            Object waitObject = new Object();
            AsynchronousEventManager eventManager = new AsynchronousEventManager();
            Thread thread = new Thread(eventManager, "Event manager thread "+ i);
            thread.start();
            System.out.println("Event manager thread started.");
            try {
                System.out.println("beginning wait");
                synchronized (waitObject) {
                    waitObject.wait(500);
                }
                System.out.println("calling shutdown");
                eventManager.shutdown();
                System.out.println("joining manager thread " + i);
                thread.join();
            } catch (InterruptedException iex) {
                iex.printStackTrace();
            }
        }
    }
}
