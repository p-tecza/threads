package examples.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class Safelock {
    
    private static class Friend {
        
        private final String name;
        private final Lock lock;
        
        public Friend(String name) {
            this.name = name;
            lock = new ReentrantLock();
        }
        
        public String getName() {
            return this.name;
        }
        
        public boolean tryBow(Friend bower) {
            boolean myLock = false;
            boolean yourLock = false;
            try {
                myLock = lock.tryLock();
                yourLock = bower.lock.tryLock();
            } finally {
                if (!(myLock && yourLock)) {
                    if (myLock) {
                        lock.unlock();
                    }
                    if (yourLock) {
                        bower.lock.unlock();
                    }
                }
            }
            return myLock && yourLock;
        }
        
        public void bow(Friend bower) {
            if (tryBow(bower)) {
                try {
                    System.out.println(name + ": " + bower.getName() + " has bowed to me!");
                    Thread.sleep(0, 1);
                    bower.bowBack(this);
                } catch (InterruptedException ie) {
                } finally {
                    lock.unlock();
                    bower.lock.unlock();
                }
            } else {
                System.out.println(name + ": " + bower.getName() + " started to"
                        + "bow to me, but saw that I was already bowing to him.");
            }
        }
        
        public void bowBack(Friend bower) {
            System.out.println(name + ": " + bower.getName() + " has bowed back to me!");
        }
    }
    
    private static class BowLoop implements Runnable {
        
        private final Friend bower;
        private final Friend bowee;
        private final Random randomGenerator;
        
        public BowLoop(Friend bower, Friend bowee) {
            this.bower = bower;
            this.bowee = bowee;
            randomGenerator = new Random();
        }
        
        @Override
        public void run() {
            for (int i = 0; i<2;i++) {
                try {
                    Thread.sleep(randomGenerator.nextInt(10));
                } catch (InterruptedException ie) {}
                bowee.bow(bower);
            }
        }
    }
    
    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        new Thread(new BowLoop(alphonse, gaston)).start();
        new Thread(new BowLoop(gaston, alphonse)).start();
    }
}
