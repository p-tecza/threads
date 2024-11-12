package myexamples;

public class ExampleThreadJoin {
    public static void main(String[] args) {
        Thread myThread = new MyThread();
        myThread.start();
        System.out.println("Czekanie na joina");
        try {
            myThread.join();
//            myThread.join(1000); //TODO tutaj po 1 sekundzie przywracana jest dalsza egzekucja w wątku macierzystym
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Doczekano sie joina");
    }
}


class MyThread extends Thread {

    @Override
    public void run() {
        try {
            System.out.println("Czekanie 1s...");
            Thread.sleep(1000);
            System.out.println("Czekanie 2s...");
            Thread.sleep(1000);
        } catch (InterruptedException ie) {

        }
    }

}

