public class Main {

    public static void main(String[] args) {
        final int N = 10;
        Incrementer r = new Incrementer();
        Thread t[] = new Thread[N];
        
        for(int n=0; n<N; n++){
            t[n] = new Thread(r);
            t[n].run();
        }
        try {
            for(int n=0; n<N; n++){
                t[n].join();
            }
        }catch (InterruptedException e){}

        System.out.println("O valor final é " + r.banco.balance());
    }
}
