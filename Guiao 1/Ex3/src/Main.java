import java.util.concurrent.locks.ReentrantLock;

// VERIFICAR , QUASE DE CERTEZA QUE NÃO ESTÁ CERTO
public class Main {

    public static void main(String[] args) {
        final int N = 10;
        Incrementer r = new Incrementer();
        ReentrantLock re = new ReentrantLock();
        Thread t[] = new Thread[N];


        for(int n=0; n<N; n++){
            t[n] = new Thread(r);
            t[n].run();
        }
        re.lock();
        try {
            for(int n=0; n<N; n++){
                t[n].join();
            }
        }catch (InterruptedException e){}
        re.unlock();
        System.out.println("O valor final é " + r.banco.balance());
    }
}