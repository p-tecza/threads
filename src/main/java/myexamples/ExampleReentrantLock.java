package myexamples;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExampleReentrantLock {
    private final Lock lock1 = new ReentrantLock();
    private final Lock lock2 = new ReentrantLock();
    public void method1() {
        lock1.lock();
        try {
            // instr1;
        } finally {
            lock1.unlock();
        }
    }
    public void method2() {
        lock2.lock();
        try {
            // instr2;
        } finally {
            lock2.unlock();
        }
    }
}