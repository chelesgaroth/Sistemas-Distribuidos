import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class Mover implements Runnable {
    Bank b;
    int s; // Number of accounts

    public Mover(Bank b, int s) { this.b=b; this.s=s; }

    public void run() {
        final int moves=100000;
        int from, to;
        Random rand = new Random();

        for (int m=0; m<moves; m++) {
            from=rand.nextInt(s); // Get one
            while ((to=rand.nextInt(s))==from); // Slow way to get distinct
            b.transfer(from,to,1);
        }
    }
}

class BankMain {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock r = new ReentrantLock();
        final int N=10;

        Bank b = new Bank(N);

        for (int i=0; i<N; i++)
            b.deposit(i,1000);

        System.out.println("Total dos saldos: " + b.totalBalance());

        Thread t1 = new Thread(new Mover(b,10));
        Thread t2 = new Thread(new Mover(b,10));

        r.lock();
        try {
            t1.start();
        }finally {
            r.unlock();
        }

        r.lock();
        try {
            t2.start();
        }finally {
            r.unlock();
        }

        t1.join();
        t2.join();

        System.out.println("Total dos saldos: " + b.totalBalance());
    }
}
