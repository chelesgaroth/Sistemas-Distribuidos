import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    int c = 0; // nº de clientes que estão sempre a chegar, é uma variável que muda
    int N = 0; // nº máx
    ReentrantLock lBarrier = new ReentrantLock();
    Condition cond = lBarrier.newCondition();

    Barrier (int num) {
        this.N = num;
    }
    void await() throws InterruptedException {
        lBarrier.lock();
        try {
            c += 1;
            if (c < N) {
                while (c < N) {
                    System.out.println(Thread.currentThread().getName() + "...Interrupted");
                    cond.await();
                }
            } else {
                cond.signalAll();
            }
        }finally {
            lBarrier.unlock();
        }
    }

}