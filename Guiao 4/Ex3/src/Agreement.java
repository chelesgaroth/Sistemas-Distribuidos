import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Agreement {
    private static class Instance{
        int result = min();
        int c = 0;
    }

    private int n;
    private Instance agmnt;

    int propose (int choice) throws InterruptedException{
        l.lock();
        try {
            Instance agmnt_t = this.agmnt;
            agmnt_t.c +=1;
            agmnt_t.result = max(agmnt_t.result,choice);
            if(agmnt_t.c < n) {
                while (this.agmnt == agmnt_t)
                    cond.await();
            }
            else{}
        }
    }
}
