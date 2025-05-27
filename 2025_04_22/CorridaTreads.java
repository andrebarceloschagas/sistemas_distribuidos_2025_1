import java.util.*;

/**
 * Programa de Corrida de Threads
 * 
 * Este programa demonstra a execução concorrente de threads Java,
 * simulando uma "corrida" entre duas threads que executam tarefas
 * paralelas com número configurável de iterações.
 * 
 * Funcionalidades:
 * - Criação de múltiplas threads trabalhadoras
 * - Sincronização com thread principal usando join()
 * - Interface interativa para configurar número de iterações
 * - Demonstração de concorrência e paralelismo
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class CorridaTreads {

	/** Scanner para entrada de dados do usuário */
	private static final Scanner scanner = new Scanner(System.in);

	/**
	 * Método principal que coordena a execução das threads.
	 * 
	 * @param args argumentos da linha de comando (não utilizados)
	 */
	public static void main(String[] args) {
		CorridaTreads corrida = new CorridaTreads();
		corrida.executarCorrida();
	}

	/**
	 * Executa a corrida de threads com interface interativa.
	 */
	public void executarCorrida() {
		try {
			exibirCabecalho();

			// Obtém número de iterações do usuário
			int numeroIteracoes = obterNumeroIteracoes();

			// Executa a corrida de threads
			executarThreads(numeroIteracoes);

		} catch (Exception e) {
			System.err.println("Erro durante execução: " + e.getMessage());
		} finally {
			scanner.close();
		}
	}

	/**
	 * Exibe o cabeçalho do programa.
	 */
	private void exibirCabecalho() {
		System.out.println("=========================================");
		System.out.println("           CORRIDA DE THREADS           ");
		System.out.println("=========================================");
		System.out.println("Este programa simula uma corrida entre");
		System.out.println("threads executando tarefas paralelas.");
		System.out.println("-----------------------------------------");
	}

	/**
	 * Obtém o número de iterações do usuário com validação.
	 * 
	 * @return número válido de iterações
	 */
	private int obterNumeroIteracoes() {
		int iteracoes;
		do {
			System.out.print("Digite o número de iterações (1-100): ");
			try {
				iteracoes = scanner.nextInt();

				if (iteracoes < 1 || iteracoes > 100) {
					System.out.println("⚠️  Número deve estar entre 1 e 100!");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("⚠️  Por favor, digite um número válido!");
				scanner.nextLine(); // Limpa entrada inválida
				iteracoes = -1; // Força nova iteração
			}
		} while (true);

		return iteracoes;
	}

	/**
	 * Cria e executa as threads trabalhadoras.
	 * 
	 * @param numeroIteracoes número de iterações para cada thread
	 */
	private void executarThreads(int numeroIteracoes) {
		System.out.println("\n🏁 Iniciando corrida com " + numeroIteracoes + " iterações...");
		System.out.println("-----------------------------------------");

		// Marca tempo de início
		long tempoInicio = System.currentTimeMillis();

		// Cria e inicia as threads competidoras
		TesteThreads threadA = new TesteThreads("🔴 Corredor-A", numeroIteracoes);
		TesteThreads threadB = new TesteThreads("🔵 Corredor-B", numeroIteracoes);

		// Inicia as threads
		threadA.start();
		threadB.start();

		// Aguarda conclusão de ambas as threads
		aguardarConclusaoThreads(threadA, threadB);

		// Calcula e exibe tempo total
		long tempoFinal = System.currentTimeMillis();
		double tempoDecorrido = (tempoFinal - tempoInicio) / 1000.0;

		System.out.println("\n=========================================");
		System.out.println("🏆 CORRIDA FINALIZADA! 🏆");
		System.out.println("=========================================");
		System.out.println("Todas as threads trabalhadoras terminaram.");
		System.out.println("Thread principal (main) finalizada!");
		System.out.println("Tempo total: " + String.format("%.2f", tempoDecorrido) + " segundos");
		System.out.println("=========================================");
	}

	/**
	 * Aguarda a conclusão de todas as threads usando join().
	 * 
	 * @param threadA primeira thread a aguardar
	 * @param threadB segunda thread a aguardar
	 */
	private void aguardarConclusaoThreads(TesteThreads threadA, TesteThreads threadB) {
		try {
			System.out.println("⏳ Thread principal aguardando conclusão das threads trabalhadoras...");

			// Aguarda thread A
			threadA.join();
			System.out.println("✅ " + threadA.getName() + " finalizou!");

			// Aguarda thread B
			threadB.join();
			System.out.println("✅ " + threadB.getName() + " finalizou!");

		} catch (InterruptedException e) {
			System.err.println("⚠️  Thread principal foi interrompida: " + e.getMessage());
			Thread.currentThread().interrupt(); // Restaura status de interrupção
		}
	}
}