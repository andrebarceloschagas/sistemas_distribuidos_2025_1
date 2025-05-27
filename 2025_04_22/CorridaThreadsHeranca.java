/**
 * Demonstração de Herança de Threads - Versão Simples
 * 
 * Este arquivo demonstra a implementação básica de threads usando herança
 * da classe Thread. Contém duas classes que trabalham juntas para mostrar
 * conceitos fundamentais de programação concorrente.
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */

/**
 * Classe que implementa uma thread trabalhadora através de herança.
 * 
 * Esta classe estende Thread e implementa o método run() para executar
 * uma série de iterações com pausas aleatórias, demonstrando o conceito
 * básico de execução concorrente.
 */
class ThreadTrabalhadora extends Thread {
    
    /** Número fixo de iterações para demonstração */
    private static final int NUMERO_ITERACOES = 5;
    
    /** Tempo máximo de pausa em milissegundos */
    private static final int TEMPO_MAXIMO_PAUSA = 2000;
    
    /**
     * Construtor que define o nome da thread.
     * 
     * @param nome nome identificador da thread
     */
    public ThreadTrabalhadora(String nome) {
        super(nome); // Chama o construtor da classe pai (Thread)
        System.out.println("🆕 Thread criada: " + nome);
    }
    
    /**
     * Método de execução principal da thread.
     * 
     * Executa 5 iterações com pausas aleatórias para demonstrar
     * o comportamento concorrente das threads.
     */
    @Override
    public void run() {
        System.out.println("▶️  " + getName() + " iniciou execução!");
        
        try {
            // Executa as iterações
            for (int etapa = 0; etapa < NUMERO_ITERACOES; etapa++) {
                // Log da etapa atual
                System.out.println(getName() + " → Executando etapa " + (etapa + 1) + 
                                 "/" + NUMERO_ITERACOES);
                
                // Pausa aleatória para simular trabalho
                int tempoPausa = (int) (Math.random() * TEMPO_MAXIMO_PAUSA);
                Thread.sleep(tempoPausa);
            }
            
            // Log de conclusão
            System.out.println("✅ " + getName() + " finalizou todas as etapas!");
            
        } catch (InterruptedException e) {
            System.err.println("⚠️  " + getName() + " foi interrompida!");
            Thread.currentThread().interrupt();
        }
    }
}

/**
 * Classe principal que demonstra o uso de threads por herança.
 * 
 * Esta classe cria e coordena múltiplas threads trabalhadoras,
 * demonstrando conceitos de sincronização usando o método join().
 */
public class CorridaThreadsHeranca {
    
    /**
     * Método principal que coordena a execução das threads.
     * 
     * @param args argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        exibirCabecalho();
        
        try {
            // Cria as threads trabalhadoras
            ThreadTrabalhadora threadA = new ThreadTrabalhadora("🔴 Trabalhador-A");
            ThreadTrabalhadora threadB = new ThreadTrabalhadora("🔵 Trabalhador-B");
            
            System.out.println("\n🏁 Iniciando execução concorrente...");
            System.out.println("-----------------------------------------");
            
            // Marca tempo de início
            long tempoInicio = System.currentTimeMillis();
            
            // Inicia as threads
            threadA.start();
            threadB.start();
            
            // Aguarda conclusão das threads
            aguardarConclusao(threadA, threadB);
            
            // Calcula tempo total
            long tempoFinal = System.currentTimeMillis();
            double tempoDecorrido = (tempoFinal - tempoInicio) / 1000.0;
            
            // Exibe relatório final
            exibirRelatorioFinal(tempoDecorrido);
            
        } catch (Exception e) {
            System.err.println("❌ Erro durante execução: " + e.getMessage());
        }
    }
    
    /**
     * Exibe o cabeçalho do programa.
     */
    private static void exibirCabecalho() {
        System.out.println("=========================================");
        System.out.println("    THREADS POR HERANÇA - DEMO         ");
        System.out.println("=========================================");
        System.out.println("Demonstração de programação concorrente");
        System.out.println("usando herança da classe Thread.");
        System.out.println("-----------------------------------------");
    }
    
    /**
     * Aguarda a conclusão de todas as threads usando join().
     * 
     * @param threadA primeira thread a aguardar
     * @param threadB segunda thread a aguardar
     */
    private static void aguardarConclusao(ThreadTrabalhadora threadA, ThreadTrabalhadora threadB) {
        try {
            System.out.println("⏳ Thread principal aguardando conclusão...");
            
            // Aguarda primeira thread
            threadA.join();
            System.out.println("🏁 " + threadA.getName() + " terminou!");
            
            // Aguarda segunda thread
            threadB.join();
            System.out.println("🏁 " + threadB.getName() + " terminou!");
            
        } catch (InterruptedException e) {
            System.err.println("⚠️  Thread principal foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Exibe o relatório final da execução.
     * 
     * @param tempoDecorrido tempo total de execução em segundos
     */
    private static void exibirRelatorioFinal(double tempoDecorrido) {
        System.out.println("\n=========================================");
        System.out.println("🏆 EXECUÇÃO CONCLUÍDA! 🏆");
        System.out.println("=========================================");
        System.out.println("Todas as threads trabalhadoras finalizaram.");
        System.out.println("Thread principal (main) terminando...");
        System.out.println("Tempo total: " + String.format("%.2f", tempoDecorrido) + " segundos");
        System.out.println("=========================================");
    }
}