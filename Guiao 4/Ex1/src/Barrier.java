import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {
    int c = 0; // nº de clientes que estão sempre a chegar, é uma variável que muda
    int N = 0; // nº máx
    int epoca = 0;
    ReentrantLock lBarrier = new ReentrantLock();
    Condition cond = lBarrier.newCondition();

    Barrier (int num) {
        this.N = num;
    }
    void await() throws InterruptedException {
        lBarrier.lock();
        try {
            c += 1;
            int e = epoca;
            if(c < N){
                while(epoca==e){
                    cond.await();
                }
            }else { // Isto é quando é a última Thread
                cond.signalAll();
                c = 0; //renovar os clientes que entram
                epoca ++; //vamos para a próxima época
            }
        }finally {
            lBarrier.unlock();
        }
    }

}
