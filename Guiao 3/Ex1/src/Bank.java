import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static class Account {
        private ReentrantLock re;
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
    private ReentrantLock reBanco;
    private ReadWriteLock rwBanco;
    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;


    /**
     * Create account and return account id
     * Lock ao Banco, de modo a criar uma conta de cada vez
     */
    public int createAccount(int balance) {
        Account c = new Account(balance);
        // reBanco.lock(); //Ex2
        rwBanco.writeLock().lock(); //Ex3
        int id;
        try {
            id = nextId;
            nextId += 1;
            map.put(id, c);
            // ou return id;
        }finally {
            //reBanco.unlock(); //Ex2
            rwBanco.writeLock().unlock(); //Ex3
        }
        return id;
    }

    /**
     * Close account and return balance, or 0 if no such account
     * Obter lockbanco para verificar se a conta existe e para proteger alterações ao hashmao.
     * Obter lockConta para impedir que se remova uma conta quando outra thread está a meio de uma operação
     * sobre essa conta.
     */

    public int closeAccount(int id) {
        //reBanco.lock(); //Ex2
        rwBanco.writeLock().lock(); //Ex3
        Account c = map.remove(id);
        if (c == null) {
            reBanco.unlock();
            return -1;
        }
        c.re.lock();
        //reBanco.unlock(); //Ex2
        rwBanco.writeLock().unlock(); //Ex3
        int saldo = c.balance;
        c.re.unlock();
        return saldo;
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        Account c;
        reBanco.lock();
        c = map.get(id);
        c.re.lock();
        reBanco.unlock();
        try {
            if (c == null)
                return 0;
            return c.balance();
        }finally {
            c.re.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        Account c;
        reBanco.lock();
        c = map.get(id);
        c.re.lock();
        reBanco.unlock();
        try {
            if (c == null)
                return false;
            return c.deposit(value);
        }finally {
            c.re.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        Account c;
        reBanco.lock();
        c = map.get(id);
        c.re.lock();
        reBanco.unlock();
        try{
            if (c == null)
                return false;
            return c.withdraw(value);
        }finally {
            c.re.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        reBanco.lock();
        cfrom = map.get(from);
        cfrom.re.lock();
        cto = map.get(to);
        cto.re.lock();
        reBanco.unlock();
        try {
            if (cfrom == null || cto ==  null)
                return false;
            return cfrom.withdraw(value) && cto.deposit(value);
        }finally {
            cfrom.re.unlock();
            cto.re.unlock();
        }
    }

    /**
     * Sum of balances in set of accounts; 0 if some does not exist
     * Obter lockBanco para obter todos os lockConta das contas válidas desejadas.
     * Consultar salto total só após ter todos os locks das contas.
     */
    public int totalBalance(int[] ids) {
        int total = 0;
        ArrayList<Integer> contasLocked = new ArrayList<>(ids.length);
        reBanco.lock();
        for(int i=0; i< ids.length; i++){
            int id = ids[i];
            if(map.containsKey(id)){
                map.get(id).re.lock();
                contasLocked.add(id);
            }
        }/* CONTINUAÇÃO ?!?!?!?!
        for (int i : ids) {
            Account c = map.get(i);
            if (c == null)
                return 0;
            total += c.balance();
        }*/
        return total;
    }

}
