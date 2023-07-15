package itm.two;

import java.util.LinkedList;
import java.util.Queue;

public class FourThreadsPlusMain {
    static final Queue<QueueElement> queue = new LinkedList<>();
    boolean fizz(int i) { return  i % 3 == 0; }
    boolean buzz(int i) { return  i % 5 == 0; }
    boolean fizzbuzz(int i) { return i % 15 == 0; }
    void number(String forOutput) { System.out.print(forOutput + " "); }


    public FourThreadsPlusMain() throws InterruptedException {

        Thread A = new Thread(() -> {
            while(true) {
                synchronized (Thread.currentThread()) {
                    // System.out.println(" Running " + Thread.currentThread().getName());
                    synchronized (queue) {
                        queue.stream()
                                .filter(e -> (e.flags & 21 ) == 0) // 1 -4 -16
                                .forEach(e -> {
                                    // System.out.println("Fizzing " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                    if (fizz(e.i)) { e.flags |= 4; e.forOutput = "fizz"; }
                                    e.flags |= 1;
                                    // System.out.println("Fizzed " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                });
                    }
                    try { Thread.currentThread().wait(); } catch (InterruptedException e) { new RuntimeException(e); }
                }
            }
        }, "A");

        Thread B = new Thread(() -> {
            while(true) {
                synchronized (Thread.currentThread()) {
                    // System.out.println(" Running " + Thread.currentThread().getName());
                    synchronized (queue) {
                        queue.stream()
                                .filter(e -> (e.flags & 22) == 0) // -2 -4 -16
                                .forEach(e -> {
                                    // System.out.println("Buzzing " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                    if (buzz(e.i)) { e.flags |= 4; e.forOutput = "buzz"; }
                                    e.flags |= 2;
                                    // System.out.println("Buzzed " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                });
                    }
                    try { Thread.currentThread().wait(); } catch (InterruptedException e) { new RuntimeException(e); }
                }
            }
        }, "B");

        Thread C = new Thread(() -> {
            while(true) {
                synchronized (Thread.currentThread()) {
                    // System.out.println(" Running " + Thread.currentThread().getName());
                    synchronized (queue) {
                        queue.stream()
                                .filter(e -> (e.flags & 28) == 4) // 4 -8 -16 C won't check it before either A or B have found something
                                .forEach(e -> {
                                    // System.out.println("Fizzbuzzing " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                    if (fizzbuzz(e.i)) e.forOutput = "fizzbuzz";
                                    e.flags |= 8;
                                    // System.out.println("Fizzbuzzed " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                });
                    }
                    try { Thread.currentThread().wait(); } catch (InterruptedException e) { new RuntimeException(e); }
                }
            }
        }, "C");

        Thread D = new Thread(() -> {
            while(true) {
                synchronized (Thread.currentThread()) {
                    // System.out.println(" Running " + Thread.currentThread().getName());
                    synchronized (queue) {
                        queue.stream()
                                .filter(e -> {
                                    // System.out.println("toPrint " + e.i + " e.flags = " + e.flags + ", e.forOutput = " + e.forOutput);
                                    return (e.flags & 23) == 3 || (e.flags & 28) == 12; // 1 +2 -4 -16 & 23 = 3   4 +8 -16 & 28 = 12
                                })
                                .forEach(e -> {
                                    number(e.forOutput + "(" +e.i + "), ");
                                    e.flags |= 16;
                                });
                    }
                    try { Thread.currentThread().wait(); } catch (InterruptedException e) { new RuntimeException(e); }
                }
            }
        }, "D");

        for(int x = 1; x <= 15;++x){
            queue.add(new QueueElement(x));
        }

        A.start();
        B.start();
        C.start();
        D.start();
        while(true) {
            Thread.sleep(1000);
            synchronized (A) { A.notify(); }
            synchronized (B) { B.notify(); }
            synchronized (C) { C.notify(); }
            synchronized (D) { D.notify(); }
        }
    }
}
