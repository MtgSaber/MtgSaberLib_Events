import net.mtgsaber.lib.events.Event;
import net.mtgsaber.lib.events.SynchronousEventManager;

/**
 * Author: Andrew Arnold (6/18/2019)
 */
public final class Test1 {
    public static final class GunShotEvent implements Event {
        public final double VOLUME;
        public final float POS_X, POS_Y, POS_Z;
        public final String SOUND_RES, NAME;

        public GunShotEvent(String name, double volume, float posX, float posY, float posZ, String soundRes) {
            this.NAME =name;
            this.VOLUME = volume;
            this.POS_X = posX;
            this.POS_Y = posY;
            this.POS_Z = posZ;
            this.SOUND_RES = soundRes;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }

    public static final class Bystander {
        private volatile float posX, posY, posZ;
        public final String NAME;

        public Bystander(float posX, float posY, float posZ, String NAME) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.NAME = NAME;
        }

        public float getPosX() { return posX; }
        public float getPosY() { return posY; }
        public float getPosZ() { return posZ; }

        public void onGunShotHeard(GunShotEvent gunShot) {
            System.out.println(NAME + " is hearing the " + gunShot.SOUND_RES + " sound file.");
        }
    }

    private static double distance3D(float x1, float y1, float z1, float x2, float y2, float z2) {
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2) + Math.pow(z1-z2, 2));
    }

    public static void main(String[] args) {
        long startTime, endTime, totalTime;
        final Bystander[] bystanders = new Bystander[] {
                new Bystander(30, 20, 20, "Joe Star"),
                new Bystander(40, 20, 20, "John Smith"),
                new Bystander(50, 20, 20, "Mary Sue"),
                new Bystander(60, 20, 20, "Gary Stu"),
        };
        final SynchronousEventManager manager = new SynchronousEventManager();
        manager.addHandler(GunShotEvent.class.getName(), e -> {
            GunShotEvent gunShot = ((GunShotEvent) e);
            for (Bystander bystander : bystanders)
                if (distance3D(
                        bystander.posX, bystander.posY, bystander.posZ,
                        gunShot.POS_X, gunShot.POS_Y, gunShot.POS_Z
                ) <= gunShot.VOLUME)
                    bystander.onGunShotHeard(gunShot);
        });
        startTime = System.nanoTime();
        manager.push(new GunShotEvent(
                GunShotEvent.class.getName(), 45, 0, 20, 20, "Gunshot.ogg"
        ));
        endTime = System.nanoTime();
        totalTime = endTime-startTime;
        System.out.println("Finished in " + totalTime + " ns! (" + (totalTime/1e+6) + "ms)");
    }
}
