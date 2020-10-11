public class Main {
	public static void main(String[] args) {
		
		final int N = 10;
		Thread t[] = new Thread[N];
		Incrementer r;
		for(int n=0; n < N; n++){
			r = new Incrementer();
			t[n] = new Thread(r);
			t[n].start();
		}
		try{
			for(int i=0; i<N; i++){
				t[i].join();
			}
		}catch (InterruptedException e){}
		
		System.out.println("Fim");
	}
}