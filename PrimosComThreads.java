import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrimosComThreads {

    public static boolean ehPrimo(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void versao1(int inicio, int fim) {
        System.out.println("Versão 1: Sem threads");
        for (int i = inicio; i <= fim; i++) {
            if (ehPrimo(i)) System.out.println(i);
        }
    }

    public static void versao2(int inicio, int fim) throws InterruptedException {
        System.out.println("Versão 2: Uma thread");
        Thread t = new Thread(() -> {
            for (int i = inicio; i <= fim; i++) {
                if (ehPrimo(i)) System.out.println(i);
            }
        });
        t.start();
        t.join();
    }

    public static void versao3(int inicio, int fim) throws InterruptedException {
        System.out.println("Versão 3: Duas threads");
        int meio = (inicio + fim) / 2;

        Thread t1 = new Thread(() -> {
            for (int i = inicio; i <= meio; i++) {
                if (ehPrimo(i)) System.out.println(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = meio + 1; i <= fim; i++) {
                if (ehPrimo(i)) System.out.println(i);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    public static void versao4(int inicio, int fim, int tamanhoSubintervalo) throws InterruptedException {
        System.out.println("Versão 4: Várias threads");
        List<Thread> threads = new ArrayList<>();

        for (int i = inicio; i <= fim; i += tamanhoSubintervalo) {
            int subInicio = i;
            int subFim = Math.min(i + tamanhoSubintervalo - 1, fim);

            Thread t = new Thread(() -> {
                for (int j = subInicio; j <= subFim; j++) {
                    if (ehPrimo(j)) System.out.println(j);
                }
            });

            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }
    }

    public static void versao5(int inicio, int fim, int tamanhoSubintervalo) throws InterruptedException {
        System.out.println("Versão 5: Lista ordenada com várias threads");
        List<Integer> primos = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();

        for (int i = inicio; i <= fim; i += tamanhoSubintervalo) {
            int subInicio = i;
            int subFim = Math.min(i + tamanhoSubintervalo - 1, fim);

            Thread t = new Thread(() -> {
                for (int j = subInicio; j <= subFim; j++) {
                    if (ehPrimo(j)) primos.add(j);
                }
            });

            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        Collections.sort(primos);
        for (int i = 0; i < primos.size(); i++) {
            System.out.println((i + 1) + ": " + primos.get(i));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int inicio = 5;
        int fim = 100;
        int tamanhoSubintervalo = 25;

        long t1 = System.currentTimeMillis();
        versao1(inicio, fim);
        System.out.println("Tempo: " + (System.currentTimeMillis() - t1) + "ms\n");

        long t2 = System.currentTimeMillis();
        versao2(inicio, fim);
        System.out.println("Tempo: " + (System.currentTimeMillis() - t2) + "ms\n");

        long t3 = System.currentTimeMillis();
        versao3(inicio, fim);
        System.out.println("Tempo: " + (System.currentTimeMillis() - t3) + "ms\n");

        long t4 = System.currentTimeMillis();
        versao4(inicio, fim, tamanhoSubintervalo);
        System.out.println("Tempo: " + (System.currentTimeMillis() - t4) + "ms\n");

        long t5 = System.currentTimeMillis();
        versao5(inicio, fim, tamanhoSubintervalo);
        System.out.println("Tempo: " + (System.currentTimeMillis() - t5) + "ms\n");
    }
}
