import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account {
        private int balance;

        Account(int balance) { this.balance = balance; }

        int balance() { return balance; }

        boolean deposit(int value) {
            balance += value;
            return true;
        }
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    // Bank slots and vector of accounts
    private int slots;
    private Account[] av;

    public Bank(int n)
    {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++) av[i]=new Account(0);
    }

    // Account balance
    public int balance(int id) {
        ReentrantLock r = new ReentrantLock();
        if (id < 0 || id >= slots)
            return 0;
        r.lock();
        try {
            return av[id].balance();
        }finally {
            r.unlock();
        }
    }

    // Deposit
    boolean deposit(int id, int value) {
        ReentrantLock r = new ReentrantLock();
        if (id < 0 || id >= slots)
            return false;
        r.lock();
        try {
            return av[id].deposit(value);
        }finally {
            r.unlock();
        }

    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        ReentrantLock r = new ReentrantLock();
        if (id < 0 || id >= slots)
            return false;
        r.lock();
        try {
            return av[id].withdraw(value);
        }finally {
            r.unlock();
        }
    }

    // Transfer
    public boolean transfer(int from, int to, int value){
        ReentrantLock r = new ReentrantLock();
        boolean retirar;
        boolean por;

        if( from <0 || to <0 || from >= slots || to >= slots) return false;
        r.lock();
        try {
            retirar = av[from].withdraw(value);
            por = av[to].deposit(value);
            return retirar && por;
        } finally {
            r.unlock();
        }
    }

    // Total Balance
    public int totalBalance(){
        ReentrantLock r = new ReentrantLock();
        int total = 0;
        r.lock();
        try {
            for(int i= 0; i<slots ; i++){
                total += av[i].balance;
            }
            return total;
        }finally {
            r.unlock();
        }
    }
}
