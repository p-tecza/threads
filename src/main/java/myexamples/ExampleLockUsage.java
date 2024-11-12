package myexamples;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExampleLockUsage{

    public static void main(String[] args){
//        ExampleLockUsage elu = new ExampleLockUsage();
        Lock lock = new ReentrantLock();
        Thread tX = new Thread(new Runner(lock, "X"));
        Thread tY = new Thread(new Runner(lock, "Y"));
        tX.start();
        tY.start();
    }
}
class Runner implements Runnable{
    private final String name;
    private final Lock lockRef;
    Runner(Lock l, String name){
        this.lockRef = l;
        this.name = name;
    }
    @Override
    public void run() {
        for(int i = 0; i < 3; i++){
            if(this.name.equals("X")){
                screamImXIfPossible();
            }else if(this.name.equals("Y")){
                screamImYIfPossible();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void screamImXIfPossible(){
        boolean lockable = lockRef.tryLock();
        if(lockable){
            try{
                System.out.println("Screaming X");
            }catch (Exception e){

            }finally{
                lockRef.unlock();
            }
        }else{
            System.out.println("Currently locked, unable to scream X!");
        }
    }
    private void screamImYIfPossible(){
        boolean lockable = lockRef.tryLock();
        if(lockable){
            try{
                System.out.println("Screaming Y");
            }catch (Exception e){

            }finally{
                lockRef.unlock();
            }
        }else{
            System.out.println("Currently locked, unable to scream Y!");
        }
    }

}
