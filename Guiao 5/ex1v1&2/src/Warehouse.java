import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Versão (1) egoísta - onde cada cliente tenta apropriar-se dos itens o mais cedo possível
 * Versão (2) cooperativa - não reserva itens para clientes que não possam ser satisfeitos no momento
 */
class Warehouse {
    private Map<String, Product> m =  new HashMap<String, Product>();
    ReentrantLock whl = new ReentrantLock();

    private class Product {
        int q = 0;
        Condition isEmpty = whl.newCondition();
    }

    private Product get(String s) {
        Product p = m.get(s);
        if (p != null) return p;
        p = new Product();
        m.put(s, p);
        return p;
    }

    public void supply(String s, int q) {
        whl.lock();
        Product p = get(s);
        p.q += q;
        p.isEmpty.signalAll();
        whl.unlock();
    }

    /**
     * Versão 1
     */
    /*
    public void consume(String[] a) throws InterruptedException {
        whl.lock();
        for (String s : a) {
            if(get(s).q==0) {
                while (get(s).q == 0) {
                    get(s).isEmpty.await();
                }
            }
            else {
                get(s).q--;
            }
        }
        whl.unlock();
    }*/

    /**
     * Versão 2
     */
    public void consume(String[] a) throws InterruptedException{
        whl.lock();
        int i=0;
        //Enquanto não tivermos atravessado a lista toda, não fazemos nenhum consumo
        while(i < a.length){
            Product p = get(a[i]);
            if(p.q==0) {
                p.isEmpty.await();
                i=0;
            }
            else i++;
        }
        for(String s : a){
            get(s).q--;
        }
        whl.unlock();
    }

    /**
     * Análise Final:
     * A versão cooperativa é melhor do que a egoísta, uma vez que otimiza o uso do armazém.
     * Há desvantagens nesta versão também, pois o cliente pode ficar sempre à espera. A este fenómeno damos o nome
     * de estado de "starvation".
     * Portanto o ideal seria nas primeiras iterações ser cooperativo mas assim que chegasse ao limite passaria a ser
     * egoísta, isto é, se ele fizesse o ciclo N vezes e ainda houvesse um produto com quantidade=0, ele iria ignorar
     * esse mesmo produto e iria consumir os que têm há em stock no armazém.
     *
     * Sugestões:
     * Tornar o stock de cada produto limitado
     * Suportar o acesso concorrente ao armazém
     * Adicionar os métodos registar e remover produtos do armazém
     */
}