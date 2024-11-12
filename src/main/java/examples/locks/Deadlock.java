/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.locks;

/**
 *
 * @author Dominik Olszewski
 */
public class Deadlock {
    
    private static class Friend {
        
        private final String name;
        
        public Friend(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public synchronized void bow(Friend bower) {
            System.out.println(name + ": " + bower.getName() + " has bowed to me!");
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException ie) {}
            bower.bowBack(this);
        }
        
        public synchronized void bowBack(Friend bower) {
            System.out.println(name + ": " + bower.getName() + " has bowed back to me!");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final Friend alphonse = new Friend("Alphonse");
        final Friend gaston = new Friend("Gaston");
        new Thread(() -> alphonse.bow(gaston)).start();
        new Thread(() -> {
//            try {
//                Thread.sleep(0, 10);
//            } catch (InterruptedException ie) {}
            gaston.bow(alphonse);
        }).start();
    }
    
}
