
/**
 * UDPServerConcorrente - Servidor UDP Concorrente para Inversão de Strings
 * 
 * Esta aplicação implementa um servidor UDP concorrente que processa múltiplas
 * requisições simultaneamente usando threads. Cada cliente recebe uma thread
 * dedicada para processar sua mensagem, permitindo alta concorrência.
 * 
 * Funcionalidades:
 * - Servidor UDP multi-threaded
 * - Inversão de strings recebidas
 * - Processamento concorrente de clientes
 * - Logs detalhados de atividade por thread
 * - Tratamento robusto de exceções
 * 
 * @author Sistema de Comunicação UDP Concorrente
 * @version 2.0
 * @since 2025-05-06
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servidor UDP concorrente que inverte strings usando threads.
 */
public class UDPServerConcorrente {

    // ==================== CONSTANTES ====================

    /** Porta padrão do servidor */
    private static final int PORTA_SERVIDOR = 6789;

    /** Tamanho do buffer para recepção */
    private static final int TAMANHO_BUFFER = 1000;

    /** Logger para registrar eventos do servidor */
    private static final Logger LOGGER = Logger.getLogger(UDPServerConcorrente.class.getName());

    // ==================== ATRIBUTOS ESTÁTICOS ====================

    /** Contador global de clientes atendidos */
    private static final AtomicInteger contadorClientes = new AtomicInteger(0);

    /** Contador global de threads ativas */
    private static final AtomicInteger threadsAtivas = new AtomicInteger(0);

    // ==================== MÉTODO PRINCIPAL ====================

    /**
     * Método principal do servidor concorrente.
     * 
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        System.out.println("=== Servidor UDP Concorrente ===");
        System.out.println("Porta: " + PORTA_SERVIDOR);
        System.out.println("Funcionalidade: Inversão de strings com threads");
        System.out.println("Pressione Ctrl+C para parar o servidor\n");

        DatagramSocket serverSocket = null;

        try {
            // Criar socket do servidor
            serverSocket = new DatagramSocket(PORTA_SERVIDOR);
            System.out.println("✓ Servidor iniciado com sucesso!");
            System.out.println("✓ Aguardando requisições de clientes...\n");

            LOGGER.info("Servidor UDP concorrente iniciado na porta " + PORTA_SERVIDOR);

            // Configurar shutdown hook
            configurarShutdownHook(serverSocket);

            // Loop principal de aceitação de clientes
            executarLoopPrincipal(serverSocket);

        } catch (SocketException e) {
            System.err.println("✗ Erro ao criar socket do servidor: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao criar socket do servidor", e);
        } finally {
            // Fechar servidor
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("\n✓ Servidor encerrado");
                System.out.println("✓ Total de clientes atendidos: " + contadorClientes.get());
                LOGGER.info("Servidor encerrado. Clientes atendidos: " + contadorClientes.get());
            }
        }
    }

    // ==================== MÉTODOS PRINCIPAIS ====================

    /**
     * Executa o loop principal de aceitação de requisições.
     * 
     * @param serverSocket Socket do servidor
     */
    private static void executarLoopPrincipal(DatagramSocket serverSocket) {
        byte[] buffer = new byte[TAMANHO_BUFFER];

        while (!serverSocket.isClosed()) {
            try {
                // Preparar datagrama para recepção
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                // Receber requisição (bloqueante)
                serverSocket.receive(receivePacket);

                int numeroCliente = contadorClientes.incrementAndGet();

                System.out.println("[Cliente #" + numeroCliente + "] Nova requisição recebida");
                System.out.println("  - Threads ativas: " + threadsAtivas.get());

                // Criar thread para processar cliente
                ClientHandler handler = new ClientHandler(serverSocket, receivePacket, numeroCliente);
                Thread clientThread = new Thread(handler, "ClientHandler-" + numeroCliente);
                clientThread.start();

                LOGGER.info("Thread criada para cliente #" + numeroCliente);

            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Erro ao receber requisição: " + e.getMessage());
                    LOGGER.log(Level.WARNING, "Erro ao receber requisição", e);
                }
            }
        }
    }

    /**
     * Configura hook para encerramento gracioso.
     * 
     * @param serverSocket Socket do servidor a ser fechado
     */
    private static void configurarShutdownHook(DatagramSocket serverSocket) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nSinal de encerramento recebido...");
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }, "ShutdownHook-UDP-Concorrente"));
    }

    // ==================== CLASSE INTERNA - CLIENT HANDLER ====================

    /**
     * Handler para processar requisições de clientes em threads separadas.
     */
    private static class ClientHandler implements Runnable {

        // ==================== ATRIBUTOS ====================

        /** Socket do servidor para envio de resposta */
        private final DatagramSocket serverSocket;

        /** Datagrama recebido do cliente */
        private final DatagramPacket receivePacket;

        /** Número identificador do cliente */
        private final int numeroCliente;

        /** Identificador da thread para logs */
        private final String identificadorThread;

        // ==================== CONSTRUTOR ====================

        /**
         * Construtor do handler de cliente.
         * 
         * @param serverSocket  Socket do servidor
         * @param receivePacket Datagrama recebido
         * @param numeroCliente Número do cliente
         */
        public ClientHandler(DatagramSocket serverSocket, DatagramPacket receivePacket, int numeroCliente) {
            this.serverSocket = serverSocket;
            this.receivePacket = receivePacket;
            this.numeroCliente = numeroCliente;
            this.identificadorThread = "[Thread-" + numeroCliente + "]";
        }

        // ==================== EXECUÇÃO DA THREAD ====================

        /**
         * Método principal de execução da thread.
         */
        @Override
        public void run() {
            int threadsAtuais = threadsAtivas.incrementAndGet();

            try {
                System.out.println(identificadorThread + " Iniciada (threads ativas: " + threadsAtuais + ")");
                LOGGER.info(identificadorThread + " Thread iniciada");

                // Processar requisição do cliente
                processarRequisicao();

                System.out.println(identificadorThread + " ✓ Processamento concluído");

            } catch (IOException e) {
                System.err.println(identificadorThread + " ✗ Erro: " + e.getMessage());
                LOGGER.log(Level.SEVERE, identificadorThread + " Erro no processamento", e);
            } finally {
                int threadsRestantes = threadsAtivas.decrementAndGet();
                System.out.println(identificadorThread + " Finalizada (threads ativas: " + threadsRestantes + ")");
                LOGGER.info(identificadorThread + " Thread finalizada");
            }
        }

        /**
         * Processa a requisição do cliente.
         * 
         * @throws IOException se houver erro na comunicação
         */
        private void processarRequisicao() throws IOException {
            // Extrair informações do cliente
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // Extrair mensagem recebida
            String receivedMessage = new String(
                    receivePacket.getData(),
                    0,
                    receivePacket.getLength(),
                    "UTF-8").trim();

            System.out.println(identificadorThread + " Processando requisição:");
            System.out.println("  - Cliente: " + clientAddress.getHostAddress() + ":" + clientPort);
            System.out.println("  - Mensagem original: \"" + receivedMessage + "\"");

            try {
                // Simular algum processamento (opcional)
                Thread.sleep(100);

                // Inverter a string recebida
                String reversedMessage = new StringBuilder(receivedMessage).reverse().toString();
                System.out.println("  - Mensagem invertida: \"" + reversedMessage + "\"");

                // Preparar resposta
                byte[] sendBuffer = reversedMessage.getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer,
                        sendBuffer.length,
                        clientAddress,
                        clientPort);

                // Enviar resposta
                System.out.print(identificadorThread + " Enviando resposta...");
                serverSocket.send(sendPacket);
                System.out.println(" ✓ Enviado!");

                LOGGER.info(identificadorThread + " Resposta enviada para " +
                        clientAddress + ":" + clientPort + " - " + reversedMessage);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException("Thread interrompida durante processamento", e);
            } catch (IOException e) {
                System.out.println(" ✗ Erro no envio!");
                throw new IOException("Erro ao enviar resposta: " + e.getMessage(), e);
            }
        }
    }
}