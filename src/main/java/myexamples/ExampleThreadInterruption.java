package myexamples;

public class ExampleThreadInterruption {

    public static void main(String[] args) {
        ThreadUno thread1 = new ThreadUno();
        ThreadDos thread2 = new ThreadDos(thread1);
        thread1.setThreadReference(thread2);
        thread1.start();
        thread2.start();
    }
}

class ThreadUno extends Thread {
    ThreadDos thread2;

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            thread2.interrupt();
            System.out.println("Interrupting thread2 and going to sleep for 1s");
//            Thread.sleep(1000); // TODO dodane po to, żeby interrupt miał możliwość się wykonać
        } catch (InterruptedException ie) {
            System.out.println("Thread2 got interrupted");
        }
    }

    public void setThreadReference(ThreadDos threadDos) {
        this.thread2 = threadDos;
    }
}

class ThreadDos extends Thread {
    private final ThreadUno thread1;

    ThreadDos(ThreadUno threadUno) {
        this.thread1 = threadUno;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            System.out.println("Thread2 got interrupted, interrupting thread1");
            this.thread1.interrupt();
        }
    }
}
