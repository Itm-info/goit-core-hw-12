package itm.one;

public class FiveSec {
    static volatile int totalTime = 0;
    public FiveSec() throws InterruptedException {
        Thread printTotal = new Thread(() -> {
            while(true) {
                synchronized (Thread.currentThread()) {
                    System.out.println(" Running for a " + totalTime + " seconds total in a thread " + Thread.currentThread().getName());
                    try {
                        Thread.currentThread().wait();
                    } catch (InterruptedException e) {
                        new RuntimeException(e);
                    }
                }
            }
        }, "printTotal");

        printTotal.start();
        while(totalTime < 20) {
            Thread.sleep(1000);
            if( ++totalTime %5==0 ) System.out.println("Минуло 5 секунд in a thread " + Thread.currentThread().getName());
            synchronized (printTotal) { printTotal.notify(); }
        }
    }
}
