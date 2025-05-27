/**
 * Classe de Thread Trabalhadora para Simulação de Corrida
 * 
 * Esta classe estende Thread e implementa uma tarefa que executa
 * um número configurável de iterações com pausas aleatórias,
 * simulando trabalho concorrente.
 * 
 * Funcionalidades:
 * - Execução de iterações configuráveis
 * - Pausas aleatórias para simular trabalho variável
 * - Logs detalhados de progresso
 * - Tratamento adequado de interrupções
 * 
 * @author Andre
 * @version 1.0
 * @since 27/05/2025
 */
public class TesteThreads extends Thread {

	/** Número de iterações que esta thread deve executar */
	private final int numeroIteracoes;

	/** Tempo máximo de pausa em milissegundos */
	private static final int TEMPO_MAXIMO_PAUSA = 1000;

	/**
	 * Construtor da thread trabalhadora.
	 * 
	 * @param nome            nome identificador da thread
	 * @param numeroIteracoes número de iterações a executar
	 * @throws IllegalArgumentException se numeroIteracoes for menor que 1
	 */
	public TesteThreads(String nome, int numeroIteracoes) {
		super(nome);

		if (numeroIteracoes < 1) {
			throw new IllegalArgumentException("Número de iterações deve ser maior que 0");
		}

		this.numeroIteracoes = numeroIteracoes;

		// Log de criação da thread
		System.out.println("🆕 Thread criada: " + nome + " (" + numeroIteracoes + " iterações)");
	}

	/**
	 * Método principal de execução da thread.
	 * 
	 * Executa o número especificado de iterações, com pausas aleatórias
	 * entre cada iteração para simular trabalho variável.
	 */
	@Override
	public void run() {
		System.out.println("▶️  " + getName() + " iniciou execução!");

		try {
			// Executa as iterações
			for (int iteracao = 0; iteracao < numeroIteracoes; iteracao++) {
				executarIteracao(iteracao);
			}

			// Log de conclusão
			System.out.println("🏁 " + getName() + " concluiu todas as " + numeroIteracoes + " iterações!");

		} catch (InterruptedException e) {
			System.err.println("⚠️  " + getName() + " foi interrompida na execução!");
			Thread.currentThread().interrupt(); // Restaura status de interrupção
		} catch (Exception e) {
			System.err.println("❌ Erro em " + getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Executa uma iteração individual com pausa aleatória.
	 * 
	 * @param numeroIteracao número da iteração atual (baseado em 0)
	 * @throws InterruptedException se a thread for interrompida durante sleep
	 */
	private void executarIteracao(int numeroIteracao) throws InterruptedException {
		// Log do progresso atual
		int porcentagem = (int) (((double) (numeroIteracao + 1) / numeroIteracoes) * 100);
		System.out.println(getName() + " → Etapa " + (numeroIteracao + 1) + "/" +
				numeroIteracoes + " (" + porcentagem + "%)");

		// Pausa aleatória para simular trabalho variável
		int tempoPausa = (int) (Math.random() * TEMPO_MAXIMO_PAUSA);

		if (tempoPausa > 0) {
			Thread.sleep(tempoPausa);
		}
	}

	/**
	 * Retorna informações sobre o estado atual da thread.
	 * 
	 * @return string com informações da thread
	 */
	@Override
	public String toString() {
		return String.format("TesteThreads{nome='%s', iteracoes=%d, estado=%s}",
				getName(), numeroIteracoes, getState());
	}
}