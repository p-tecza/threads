package myexamples;

public class ExampleThread extends Thread {

    private final String name;
    private int cnt = 0;
    public static int sharedCounter = 0;

    private final Object lockObject = new Object();

    public ExampleThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 2; i++) {
                System.out.println(name + " myCounter: " + cnt);
                System.out.println(name + " sharedCounter: " + sharedCounter);
                cnt++;
//                Thread.sleep(250);
                synchronized (lockObject){
                    sharedCounter++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }finally {
            System.out.println(name + " stan koncowy sharedCounter: " + sharedCounter);
        }
    }

    public static void main(String[] args) {
        ExampleThread firstThread = new ExampleThread("Pierwszy");
        ExampleThread secondThread = new ExampleThread("Drugi");
        ExampleThread thirdThread = new ExampleThread("Trzeci");
        ExampleThread fourthThread = new ExampleThread("Czwarty");
        ExampleThread fifthThread = new ExampleThread("Piąty");

        firstThread.start();
        secondThread.start();
        thirdThread.start();
        fourthThread.start();
        fifthThread.start();


        State threadState = firstThread.getState();
        System.out.println("Stan wątku: "+ threadState);
    }


}
