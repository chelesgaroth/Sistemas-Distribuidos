public class Incrementer implements Runnable {
    Bank banco = new Bank();
    public void run() {
        final int I = 1000;
        final int V = 100;
        for(int i=0; i<I; i++){
            banco.deposit(V);
        }
    }
}
