public class TesteThreads extends Thread {
    // Construtor da classe TesteThreads
    public TesteThreads(String nome) {
        // Chama o construtor da classe pai (Thread) para definir o nome da thread.
        // Isso é útil para identificar diferentes threads durante a execução.
        super(nome); 
    }

    // O método run() contém o código que será executado pela thread.
    // Quando você chama o método start() em um objeto TesteThreads, 
    // a JVM chama este método run() em uma nova thread de execução.
    public void run() {
        // Loop que executa 5 vezes.
        for (int i = 0; i < 5; i++) {
            // Imprime o nome da thread (obtido com getName()) e a etapa atual do loop.
            System.out.println(getName() + " na etapa " + i); 
            try {
                // Pausa a execução da thread atual por um período aleatório.
                // Math.random() gera um número entre 0.0 e 1.0 (exclusive).
                // Multiplicado por 2000, resulta em um valor entre 0 e 1999.
                // O (int) converte o resultado para um inteiro (milissegundos).
                // A thread dormirá por até 2 segundos.
                sleep((int) (Math.random() * 2000)); 
            } catch (Exception e) {
                // O método sleep() pode lançar uma InterruptedException se a thread
                // for interrompida enquanto dorme. Este bloco catch captura essa
                // (e outras) exceções. 
                // A linha e.printStackTrace() está comentada, então a exceção é
                // capturada mas nenhuma ação é tomada (geralmente não recomendado).
                // É uma boa prática tratar a exceção ou pelo menos logá-la.
                // e.printStackTrace(); 
            }
        }
        // Mensagem impressa quando o loop termina, indicando que a thread concluiu sua tarefa.
        System.out.println("Thread terminada: " + getName()); 
    }
}